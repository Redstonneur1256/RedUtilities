package fr.redstonneur1256.redutilities.async;

import fr.redstonneur1256.redutilities.function.UnsafeProvider;

public interface ThreadPool {

    boolean isActive();

    default <T> Task<T> execute(UnsafeProvider<T, ?> runnable) {
        Task<T> task = new Task<>();
        execute(() -> {
            T value;
            try {
                value = runnable.get();
            }catch(Throwable exception) {
                task.fail(exception);
                return;
            }
            task.complete(value);
        });
        return task;
    }

    void execute(Runnable runnable);

    void shutdown();

}
