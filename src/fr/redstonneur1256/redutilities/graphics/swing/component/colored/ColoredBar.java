package fr.redstonneur1256.redutilities.graphics.swing.component.colored;

import fr.redstonneur1256.redutilities.graphics.ImageHelper;
import fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp.AbstractBar;

import java.awt.*;
import java.util.Objects;

public class ColoredBar extends AbstractBar {

    private Color emptyColor;
    private Color filledColor;

    public ColoredBar(Color emptyColor) {
        this(emptyColor, null);
    }

    public ColoredBar(Color emptyColor, Color filledColor) {
        Objects.requireNonNull(emptyColor, "The background color cannot be null");
        this.emptyColor = emptyColor;
        this.filledColor = filledColor == null ? emptyColor.brighter() : filledColor;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        graphics.setColor(emptyColor);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        int size = getFill();
        if(size > 0) {
            graphics.setColor(filledColor);
            graphics.fillRect(0, 0, isVertical() ? getWidth() : size, isVertical() ? size : getHeight());
        }
        if(isStringPainted() && getString() != null) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setFont(getFont());
            graphics.setColor(getStringColor());
            ImageHelper.drawCenterText(graphics, getString(), getWidth() / 2, getHeight() / 2);
        }
    }

    public Color getEmptyColor() {
        return emptyColor;
    }

    public void setEmptyColor(Color emptyColor) {
        Objects.requireNonNull(emptyColor, "The background color cannot be null");
        this.emptyColor = emptyColor;
    }

    public Color getFilledColor() {
        return filledColor;
    }

    public void setFilledColor(Color filledColor) {
        Objects.requireNonNull(filledColor, "The color cannot be null");
        this.filledColor = filledColor;
    }
}
