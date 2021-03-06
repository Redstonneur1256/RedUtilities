package fr.redstonneur1256.redutilities.graphics;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.util.Arrays;

public class ImageHelper {

    private static final ColorConvertOp grayConvertor;

    static {
        grayConvertor = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    }

    public static BufferedImage color(BufferedImage image, Color color) {
        image = copy(image);
        Graphics graphics = image.getGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
        return image;
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        return resize(image, width, height, image.getType());
    }

    public static BufferedImage resize(BufferedImage image, int width, int height, int type) {
        BufferedImage copy = new BufferedImage(width, height, type);
        Graphics graphics = copy.getGraphics();
        graphics.drawImage(image, 0, 0, width, height, null);
        graphics.dispose();
        return copy;
    }

    public static BufferedImage changeFormat(BufferedImage image, int newImageType) {
        return resize(image, image.getWidth(), image.getHeight(), newImageType);
    }

    public static BufferedImage copy(BufferedImage original) {
        return resize(original, original.getWidth(), original.getHeight());
    }

    public static int getAverageColor(BufferedImage image) {
        int a = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        boolean hasAlpha = hasAlpha(image);
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                a += hasAlpha ? (rgb >> 24) & 0xFF : 0xFF;
                r += (rgb >> 16) & 0xFF;
                g += (rgb >> 8) & 0xFF;
                b += (rgb) & 0xFF;
            }
        }

        float size = (image.getWidth() * image.getHeight());

        a = (int) Math.floor(a / size);
        r = (int) Math.floor(r / size);
        g = (int) Math.floor(g / size);
        b = (int) Math.floor(b / size);

        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);

    }

    public static int getOpposite(int color) {
        float[] hsb = Color.RGBtoHSB(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, null);

        float brightness = hsb[2];

        if(brightness == 0) {
            return 0xFFFFFF; // White
        }
        if(brightness == 1) {
            return 0x000000; // Black
        }

        if(brightness < 0.5F) {
            hsb[2] = (brightness + 0.5F) % 1.0F;
        }

        return Color.HSBtoRGB((hsb[0] + 0.5F) % 1.0F, hsb[1], hsb[2]);
    }

    public static boolean hasAlpha(BufferedImage image) {
        int type = image.getType();
        return type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_ARGB_PRE ||
                type == BufferedImage.TYPE_4BYTE_ABGR || type == BufferedImage.TYPE_4BYTE_ABGR_PRE;
    }

    public static void drawCenteredImageRounded(Graphics graphics, BufferedImage image, int x, int y, ImageObserver observer) {
        drawCenteredImageRounded(graphics, image, x, y, image.getHeight(), image.getWidth(), observer);
    }

    public static void drawCenteredImageRounded(Graphics graphics, BufferedImage image, int x, int y, int width, int height, ImageObserver observer) {
        Shape shape = graphics.getClip();
        int xx = x - (width / 2);
        int yy = y - (height / 2);
        graphics.setClip(new Ellipse2D.Double(xx, yy, height, width));
        graphics.drawImage(image, xx, yy, width, height, observer);
        graphics.setClip(shape);
    }

    public static void ellipse(Graphics2D graphics, int x, int y, int width, int height) {
        Shape shape = graphics.getClip();
        int xx = x - (width / 2);
        int yy = y - (height / 2);
        graphics.setClip(new Ellipse2D.Double(xx, yy, height, width));
        graphics.fillOval(xx, yy, width, height);
        graphics.setClip(shape);
    }

    public static void drawCenteredImage(Graphics graphics, BufferedImage image, int x, int y, ImageObserver observer) {
        drawCenteredImage(graphics, image, x, y, image.getHeight(), image.getWidth(), observer);
    }

    public static void drawCenteredImage(Graphics graphics, BufferedImage image, int x, int y, int width, int height, ImageObserver observer) {
        int xx = x - (width / 2);
        int yy = y - (height / 2);
        graphics.drawImage(image, xx, yy, width, height, observer);
    }

    public static void drawCenterText(Graphics graphics, String text, int x, int y) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle2D bounds = fontMetrics.getStringBounds(text, graphics);
        int width = (int) bounds.getWidth();
        graphics.drawString(text, x - (width / 2), y + fontMetrics.getDescent());
    }

    public static void drawCenterTextShadowed(Graphics2D graphics, String text, int x, int y) {
        TextLayout layout = new TextLayout(text, graphics.getFont(), graphics.getFontRenderContext());
        Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(text, graphics);
        int stringWidth = (int) bounds.getWidth();
        int stringHeight = (int) bounds.getHeight();

        BufferedImage shadowImage = new BufferedImage(stringWidth, stringHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D temp = (Graphics2D) shadowImage.getGraphics();
        temp.setColor(Color.BLACK);
        temp.setFont(graphics.getFont());
        temp.drawString(text, 0, temp.getFontMetrics().getAscent());
        temp.dispose();

        int size = 5;
        float[] data = new float[stringWidth * stringHeight];
        Arrays.fill(data, 1F / (size * size));

        Kernel kernel = new Kernel(size, size, data);
        BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage blurredShadow = op.filter(shadowImage, null);

        graphics.drawImage(blurredShadow, x - (stringWidth / 2) + 3, y - temp.getFontMetrics().getAscent() + 3, null);

        graphics.setPaint(Color.WHITE);
        layout.draw(graphics, x - (stringWidth / 2F), y);
    }

    public static BufferedImage convertBlackAndWhite(BufferedImage image) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        grayConvertor.filter(image, output);
        return output;
    }

}
