package fr.redstonneur1256.redutilities;

import java.awt.*;

public class Maths {

    public static byte sum(byte[] values) {
        byte total = 0;
        for(byte value : values) {
            total += value;
        }
        return total;
    }

    public static short sum(short[] values) {
        short total = 0;
        for(short value : values) {
            total += value;
        }
        return total;
    }

    public static int sum(int[] values) {
        int total = 0;
        for(int value : values) {
            total += value;
        }
        return total;
    }

    public static long sum(long[] values) {
        long total = 0;
        for(long value : values) {
            total += value;
        }
        return total;
    }

    public static double sum(double[] values) {
        double total = 0;
        for(double value : values) {
            total += value;
        }
        return total;
    }

    public static float sum(float[] values) {
        float total = 0;
        for(float value : values) {
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

    /**
     * @see Maths#distance(double, double, double, double)
     */
    public static double distance(Point a, Point b) {
        return distance(a.x, a.y, b.x, b.y);
    }

    /**
     * Return the euclidean distance between the point (x1,y1) and (x2,y2)
     *
     * @param x1 the X coordinates of the first point
     * @param y1 the Y coordinates of the first point
     * @param x2 the X coordinates of the second point
     * @param y2 the Y coordinates of the second point
     * @return The distance
     * @see Maths#distanceSquared(double, double, double, double)
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distanceSquared(x1, y1, x2, y2));
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static int min(int... values) {
        int min = values[0];
        for(int value : values) {
            min = Math.min(min, value);
        }
        return min;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static long min(long... values) {
        long min = values[0];
        for(long value : values) {
            min = Math.min(min, value);
        }
        return min;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static float min(float... values) {
        float min = values[0];
        for(float value : values) {
            min = Math.min(min, value);
        }
        return min;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static double min(double... values) {
        double min = values[0];
        for(double value : values) {
            min = Math.min(min, value);
        }
        return min;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static int max(int... values) {
        int max = values[0];
        for(int value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static long max(long... values) {
        long max = values[0];
        for(long value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static float max(float... values) {
        float max = values[0];
        for(float value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the values are empty
     */
    public static double max(double... values) {
        double max = values[0];
        for(double value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    public static int toInt(long value) {
        if(value > Integer.MAX_VALUE) {
            value = Integer.MAX_VALUE;
        }else if(value < Integer.MIN_VALUE) {
            value = Integer.MIN_VALUE;
        }
        return (int) value;
    }

    public static float toFloat(double value) {
        if(value > Float.MAX_VALUE) {
            value = Float.MAX_VALUE;
        }else if(value < Float.MIN_VALUE) {
            value = Float.MIN_VALUE;
        }
        return (float) value;
    }

}
