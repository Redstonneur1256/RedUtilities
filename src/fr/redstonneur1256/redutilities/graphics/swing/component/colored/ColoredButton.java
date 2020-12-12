package fr.redstonneur1256.redutilities.graphics.swing.component.colored;

import fr.redstonneur1256.redutilities.graphics.ImageHelper;
import fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp.AbstractButton;

import java.awt.*;
import java.util.Objects;

public class ColoredButton extends AbstractButton {

    private Color color;
    private Color hoverColor;
    private Color disabledColor;

    public ColoredButton(Color color) {
        this(color, null, null);
    }

    public ColoredButton(Color color, Color hoverColor) {
        this(color, hoverColor, null);
    }

    public ColoredButton(Color color, Color hoverColor, Color disabledColor) {
        Objects.requireNonNull(color, "Color cannot be null");
        this.color = color;
        this.hoverColor = hoverColor == null ? color.brighter() : hoverColor;
        this.disabledColor = disabledColor == null ? color.darker() : disabledColor;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Color color = isEnabled() ? (isHovering() ? hoverColor : this.color) : disabledColor;

        graphics.setColor(color);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        if(getText() != null) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setFont(getFont());
            g.setColor(getTextColor());
            ImageHelper.drawCenterText(graphics, getText(), getWidth() / 2, getHeight() / 2);
        }

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.repaint();
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
        this.repaint();
    }

    public Color getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(Color disabledColor) {
        this.disabledColor = disabledColor;
        this.repaint();
    }
}
