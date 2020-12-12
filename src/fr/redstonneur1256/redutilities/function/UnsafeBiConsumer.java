package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface UnsafeBiConsumer<A, B, E extends Exception> {

    void consume(A a, B b) throws E;

    default UnsafeBiConsumer<A, B, E> with(UnsafeBiConsumer<A, B, E> other) {
        return (a, b) -> {
            consume(a, b);
            other.consume(a, b);
        };
    }

}
