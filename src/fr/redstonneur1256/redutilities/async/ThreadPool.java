package fr.redstonneur1256.redutilities.async;

import fr.redstonneur1256.redutilities.function.Provider;

public interface ThreadPool {

    boolean isActive();

    <T> Task<T> execute(Provider<T> runnable);

    void execute(Runnable runnable);

    void shutdown();

}
