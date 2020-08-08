package fr.redstonneur1256.redutilities;

public class MathUtils {

    public static byte sum(byte[] values) {
        byte total = 0;
        for (byte value : values) {
            total += value;
        }
        return total;
    }

    public static short sum(short[] values) {
        short total = 0;
        for (short value : values) {
            total += value;
        }
        return total;
    }

    public static int sum(int[] values) {
        int total = 0;
        for (int value : values) {
            total += value;
        }
        return total;
    }

    public static long sum(long[] values) {
        long total = 0;
        for (long value : values) {
            total += value;
        }
        return total;
    }

    public static double sum(double[] values) {
        double total = 0;
        for (double value : values) {
            total += value;
        }
        return total;
    }

    public static float sum(float[] values) {
        float total = 0;
        for (float value : values) {
            total += value;
        }
        return total;
    }

    public static int average(int[] values) {
        return sum(values) / values.length;
    }

    public static long average(long[] values) {
        return sum(values) / values.length;
    }

    public static double average(double[] values) {
        return sum(values) / values.length;
    }

    public static float average(float[] values) {
        return sum(values) / values.length;
    }

}
