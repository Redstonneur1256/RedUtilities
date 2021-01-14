package fr.redstonneur1256.redutilities.async.pools;

import fr.redstonneur1256.redutilities.async.ThreadPool;
import fr.redstonneur1256.redutilities.async.Threads;
import fr.redstonneur1256.redutilities.function.Functions;
import fr.redstonneur1256.redutilities.function.UnsafeRunnable;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;

public class ReusableThreadPool implements ThreadPool {

    private final ThreadFactory factory;
    private final int maxThreads;
    private final List<ThreadWorker> threads;
    private final Queue<ThreadWorker> freeThreads;
    private final Queue<Runnable> queue;

    public ReusableThreadPool() {
        this(Threads::daemon, Runtime.getRuntime().availableProcessors() * 2);
    }

    public ReusableThreadPool(int maxThreads) {
        this(Threads::daemon, maxThreads);
    }

    public ReusableThreadPool(ThreadFactory factory) {
        this(factory, Runtime.getRuntime().availableProcessors() * 2);
    }

    public ReusableThreadPool(ThreadFactory factory, int maxThreads) {
        this.factory = factory;
        this.maxThreads = maxThreads;
        this.threads = new CopyOnWriteArrayList<>();
        this.freeThreads = new ConcurrentLinkedQueue<>();
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void execute(Runnable runnable) {
        ThreadWorker worker = freeThreads.poll();
        if(worker == null) {
            worker = newWorker();
        }
        if(worker == null) {
            synchronized(queue) {
                queue.add(runnable);
            }
            return;
        }

        worker.postCommand(runnable);
    }

    @Override
    public void shutdown() {
        for(ThreadWorker thread : threads) {
            thread.thread.interrupt();
        }
        threads.clear();
        freeThreads.clear();
    }

    private ThreadWorker newWorker() {
        int id = threads.size();

        if(id >= maxThreads) {
            return null; // No threads available
        }

        ThreadWorker worker = new ThreadWorker(id);
        worker.start();
        threads.add(worker);
        freeThreads.add(worker);
        return worker;
    }

    private class ThreadWorker {

        private final Object lock;
        private final int id;
        private Thread thread;
        private Runnable command;

        public ThreadWorker(int id) {
            this.lock = new Object();
            this.id = id;
        }

        private void start() {
            thread = factory.newThread(this::run);
            thread.setName("Worker-" + id);
        }

        public boolean isUsed() {
            synchronized(lock) {
                return command != null;
            }
        }

        public void postCommand(Runnable command) {
            synchronized(lock) {
                if(this.command != null) {
                    throw new IllegalStateException("The worker is already used");
                }
                freeThreads.remove(this);

                this.command = command;
                this.lock.notifyAll();
            }
        }

        private void run() {
            while(!thread.isInterrupted()) {
                synchronized(lock) {
                    if(command == null) {
                        Functions.runtime((UnsafeRunnable<Throwable>) lock::wait);
                    }

                    try {
                        command.run();
                    }catch(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    command = null;

                    synchronized(queue) {
                        if(!queue.isEmpty()) {
                            postCommand(queue.poll());
                            continue;
                        }
                    }

                    freeThreads.add(this);
                }
            }
        }

    }

}
