package fr.redstonneur1256.redutilities.async.pools;

import fr.redstonneur1256.redutilities.async.Task;
import fr.redstonneur1256.redutilities.async.ThreadPool;
import fr.redstonneur1256.redutilities.async.Threads;
import fr.redstonneur1256.redutilities.function.Provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorThreadPool implements ThreadPool {

    private ExecutorService executor;

    public ExecutorThreadPool() {
        this(Runtime.getRuntime().availableProcessors() * 2);
    }

    public ExecutorThreadPool(int threads) {
        this(threads, false, null);
    }

    public ExecutorThreadPool(int threads, boolean daemon) {
        this(threads, daemon, null);
    }

    public ExecutorThreadPool(int threads, boolean daemon, String name) {
        executor = Executors.newFixedThreadPool(threads, runnable -> Threads.create(name, runnable, daemon));
    }

    @Override
    public boolean isActive() {
        return !executor.isShutdown();
    }

    @Override
    public <T> Task<T> execute(Provider<T> runnable) {
        Task<T> task = new Task<>();
        execute(() -> task.complete(runnable.get()));
        return task;
    }

    @Override
    public void execute(Runnable runnable) {
        executor.submit(runnable);
    }

    public void shutdown() {
        executor.shutdown();
    }

}