package fr.redstonneur1256.redutilities.graphics.swing.component.colored;

import fr.redstonneur1256.redutilities.graphics.ImageHelper;
import fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp.AbstractLabel;
import org.apache.commons.lang3.Validate;

import java.awt.*;

public class ColoredLabel extends AbstractLabel {

    private Color textColor;

    public ColoredLabel() {
        this("", Color.BLACK);
    }

    public ColoredLabel(String text) {
        this(text, Color.BLACK);
    }

    public ColoredLabel(String text, Color textColor) {
        super(text);
        this.textColor = textColor;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setFont(getFont());
        graphics.setColor(textColor);
        ImageHelper.drawCenterText(graphics, getText(), getWidth() / 2, getHeight() / 2);
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        Validate.notNull(textColor, "TextColor cannot be null");
        this.textColor = textColor;
        this.repaint();
    }

}
