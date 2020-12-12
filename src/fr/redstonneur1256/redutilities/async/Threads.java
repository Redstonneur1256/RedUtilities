package fr.redstonneur1256.redutilities.async;

import fr.redstonneur1256.redutilities.function.Functions;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Threads {

    public static Thread daemon(Runnable runnable) {
        return daemon(thread -> runnable.run());
    }

    public static Thread daemon(String name, Runnable runnable) {
        return daemon(name, thread -> runnable.run());
    }

    public static Thread daemon(Consumer<Thread> runnable) {
        return daemon(null, runnable);
    }

    public static Thread daemon(String name, Consumer<Thread> runnable) {
        return create(name, runnable, true);
    }

    public static Thread create(Runnable runnable) {
        return create(thread -> runnable.run());
    }

    public static Thread create(String name, Runnable runnable) {
        return create(name, thread -> runnable.run());
    }

    public static Thread create(Consumer<Thread> runnable) {
        return create(null, runnable);
    }

    public static Thread create(String name, Consumer<Thread> runnable) {
        return create(name, runnable, false);
    }

    public static Thread create(String name, Runnable runnable, boolean daemon) {
        return create(name, thread -> runnable.run(), daemon);
    }

    public static Thread create(String name, Consumer<Thread> runnable, boolean daemon) {
        Thread thread = new Thread(() -> runnable.accept(Thread.currentThread()));
        if(name != null) {
            thread.setName(name);
        }
        thread.setDaemon(daemon);
        thread.start();
        return thread;
    }

    public static Thread current() {
        return Thread.currentThread();
    }

    public static boolean interrupted() {
        return Thread.currentThread().isInterrupted();
    }

    public static void sleep(long amount, TimeUnit unit) {
        sleep(unit.toMillis(amount));
    }

    public static void sleep(TimeUnit unit, long amount) {
        sleep(unit.toMillis(amount));
    }

    public static void sleep(long millis) {
        sleep(millis, 0);
    }

    public static void sleep(long millis, int nanos) {
        Functions.quiet(() -> Thread.sleep(millis, nanos));
    }

}
