package fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp;

import org.apache.commons.lang3.Validate;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AbstractBar extends JComponent {

    private int value;
    private int maximum;
    private String string;
    private boolean stringPainted;
    private Color stringColor;
    private boolean vertical;

    public AbstractBar() {
        value = 0;
        maximum = 100;
        string = "";
        stringPainted = false;
        stringColor = Color.BLACK;
        vertical = false;
    }

    protected int getFill() {
        double value = (double) this.value / this.maximum;
        return (int) Math.round(value * (isVertical() ? getHeight() : getWidth()));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if(value == this.value)
            return;

        Validate.isTrue(value >= 0 && value <= maximum, "Value must be >= 0 and <= maximum");
        this.value = value;
        this.repaint();
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        Validate.isTrue(maximum >= 1, "The maximum must be >= 1");
        this.maximum = maximum;
        this.repaint();
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        Objects.requireNonNull(string, "string == null");
        this.string = string;
        this.repaint();
    }

    public boolean isStringPainted() {
        return stringPainted;
    }

    public void setStringPainted(boolean stringPainted) {
        this.stringPainted = stringPainted;
    }

    public Color getStringColor() {
        return stringColor;
    }

    public void setStringColor(Color stringColor) {
        this.stringColor = stringColor;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public interface Listener {

        void valueChanged(int value);

    }

    public static class ListenerAdapter implements Listener {

        @Override
        public void valueChanged(int value) {

        }

    }

}
