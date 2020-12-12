package fr.redstonneur1256.redutilities;

public class Validate {

    public static void isTrue(boolean value, String message) {
        if(!value) {
            throw new IllegalStateException(message);
        }
    }

    public static void notEmpty(String string, String message) {
        isTrue(!string.isEmpty(), message);
    }

}
