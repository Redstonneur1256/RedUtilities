package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface UnsafeRunnable<E extends Throwable> {

    void run() throws E;

    default UnsafeRunnable<E> with(UnsafeRunnable<E> other) {
        return () -> {
            run();
            other.run();
        };
    }

}
