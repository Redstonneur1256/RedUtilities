package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface Provider<O> {

    O get();

}
