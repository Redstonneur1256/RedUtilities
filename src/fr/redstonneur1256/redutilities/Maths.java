package fr.redstonneur1256.redutilities;

import java.awt.*;

public class Maths {

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

    public static int average(byte[] values) {
        return sum(values) / values.length;
    }

    public static int average(short[] values) {
        return sum(values) / values.length;
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

    public static double distanceSquared(Point a, Point b) {
        return distanceSquared(a.x, a.y, b.x, b.y);
    }

    public static double distanceSquared(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2);
    }

    public static double distance(Point a, Point b) {
        return distance(a.x, a.y, b.x, b.y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSquared(x1, y1, x2, y2));
    }

}
