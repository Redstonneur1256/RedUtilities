package fr.redstonneur1256.redutilities.function;

@FunctionalInterface
public interface UnsafeBiFunction<I1, I2, O, E extends Throwable> {

    O apply(I1 i1, I2 i2) throws E;

}
