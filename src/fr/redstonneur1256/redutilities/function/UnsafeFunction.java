package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface UnsafeFunction<I, O, E extends Throwable> {

    O apply(I i) throws E;

}
