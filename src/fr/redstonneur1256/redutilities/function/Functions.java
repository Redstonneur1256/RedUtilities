package fr.redstonneur1256.redutilities.function;

import java.util.function.Function;

public class Functions {

    public static boolean quiet(UnsafeRunnable<?> runnable) {
        try {
            runnable.run();
            return true;
        }catch(Throwable throwable) {
            return false;
        }
    }

    public static void runtime(UnsafeRunnable<?> runnable) {
        try {
            runnable.run();
        }catch(Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T> T runtime(UnsafeProvider<T, ?> supplier) {
        try {
            return supplier.get();
        }catch(Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <I, O> O runtime(UnsafeFunction<I, O, ?> function, I in) {
        try {
            return function.apply(in);
        }catch(Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <I, O, E extends Throwable> Function<I, O> wrapped(UnsafeFunction<I, O, E> function) {
        return i -> runtime(function, i);
    }

}
