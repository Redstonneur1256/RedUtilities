package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface UnsafeConsumer<I, E extends Throwable> {

    void consume(I i) throws E;

    default UnsafeConsumer<I, E> with(UnsafeConsumer<I, E> other) {
        return (i) -> {
            consume(i);
            other.consume(i);
        };
    }

}
