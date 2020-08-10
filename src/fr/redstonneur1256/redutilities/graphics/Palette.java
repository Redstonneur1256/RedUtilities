package fr.redstonneur1256.redutilities.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Palette<T extends Palette.ColorContainer> {

    private final List<T> colors;
    private final Map<Integer, T> cachedColors;
    private final Function<Integer, T> cacheLoader;
    private T defaultValue;
    private boolean useCache;

    public Palette() {
        this(null);
    }
    public Palette(T defaultValue) {
        this.colors = new ArrayList<>();
        this.cachedColors = new HashMap<>();
        this.cacheLoader = this::matchColor0;
        this.defaultValue = defaultValue;
    }

    public Palette<T> useCache(boolean use) {
        this.useCache = use;
        return this;
    }

    public boolean useCache() { return this.useCache; }

    public Palette<T> addColor(T t) {
        this.colors.add(t);
        return this;
    }

    public List<T> getColors() {
        return colors;
    }

    public void importFrom(File file, Function<Color, T> supplier) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        int count = input.readInt();
        for (int i = 0; i < count; i++) {
            int color = input.readInt();
            colors.add(supplier.apply(new Color(color)));
        }
        input.close();
        System.out.printf("Imported %s colors from %s, Total colors: %s%n", count, file, colors.size());
    }

    public void exportTo(File file) throws Exception {
        DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
        output.writeInt(colors.size());
        for (T data : colors) {
            output.writeInt(data.getColorRGB());
        }
        output.flush();
        output.close();
        System.out.println("Exported " + colors.size() + " colors to file " + file);
    }

    public T matchColor(int rgb) {
        return useCache ? cachedColors.computeIfAbsent(rgb, cacheLoader) : matchColor0(rgb);
    }

    public BufferedImage toImage(T[] data, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x + y * width;
                T t = data[index];
                if (t == null)
                    continue;

                pixels[index] = t.getColorRGB();
            }
        }
        return image;
    }

    /* Internal methods */

    private T matchColor0(int rgb) {
        T closest = defaultValue;
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        double closestDistance = Double.POSITIVE_INFINITY;
        for (T container : colors) {
            int c2 = container.getColorRGB();
            int r2 = (c2 >> 16) & 0xFF;
            int g2 = (c2 >> 8) & 0xFF;
            int b2 = c2 & 0xFF;
            double distance = getDistance(red, green, blue, r2, g2, b2);
            if (distance < closestDistance) {
                closest = container;
                closestDistance = distance;
            }
        }
        return closest;
    }

    private static double getDistance(int r1, int g1, int b1, int r2, int g2, int b2) {
        int dr = r1 - r2;
        int dg = g1 - g2;
        int db = b1 - b2;

        double redMean = (r1 + r2) / 2.0D;
        double weightR = 2.0D + redMean / 256.0D;
        double weightG = 4.0D;
        double weightB = 2.0D + (255.0D - redMean) / 256.0D;
        return (int) (weightR * dr * dr + weightG * dg * dg + weightB * db * db);
    }

    public static class ColorContainer {
        private Color color;

        public ColorContainer() {
        }

        public ColorContainer(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public int getColorRGB() {
            return color.getRGB();
        }

    }
}