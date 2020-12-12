package fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbstractButton extends JComponent implements MouseListener {

    private String text;
    private Color textColor;
    private List<Runnable> eventListeners;
    private boolean hovering;

    public AbstractButton() {
        text = "";
        textColor = Color.BLACK;
        eventListeners = new ArrayList<>();
        hovering = false;

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(isEnabled()) {
            eventListeners.forEach(Runnable::run);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hovering = true;
        this.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hovering = false;
        this.repaint();
    }

    public void addListener(Runnable listener) {
        eventListeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        eventListeners.remove(listener);
    }

    public List<Runnable> getEventListeners() {
        return eventListeners;
    }

    public boolean isHovering() {
        return hovering;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        Objects.requireNonNull(text, "Text cannot be null");
        this.text = text;
        this.repaint();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        Objects.requireNonNull(textColor, "Text color cannot be null");
        this.textColor = textColor;
        this.repaint();
    }
}
