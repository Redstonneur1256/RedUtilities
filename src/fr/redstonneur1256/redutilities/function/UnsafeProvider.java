package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface UnsafeProvider<O, E extends Throwable> {

    O get() throws E;

}
