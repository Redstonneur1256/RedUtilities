package fr.redstonneur1256.redutilities.graphics.swing.component.colored;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class ColoredSelectionBar extends ColoredBar implements MouseListener, MouseMotionListener {

    private boolean dragging;
    private List<Listener> changeListeners;
    public ColoredSelectionBar(Color emptyColor) {
        super(emptyColor);
        init();
    }

    public ColoredSelectionBar(Color emptyColor, Color filledColor) {
        super(emptyColor, filledColor);
        init();
    }

    private void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
        changeListeners = new ArrayList<>();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(!enabled && dragging) {
            dragging = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        onMoved(e.getX(), e.getY());
        dragging = true;
    }

    public List<Listener> getChangeListeners() {
        return changeListeners;
    }

    public void addChangeListener(Listener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(Listener listener) {
        changeListeners.remove(listener);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        onMoved(e.getX(), e.getY());
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        onMoved(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void onMoved(int x, int y) {
        if(!isEnabled())
            return;

        int position = isVertical() ? y : x;
        int max = isVertical() ? getHeight() : getWidth();
        double value = position / (double) max;
        value = Math.min(1, Math.max(0, value));
        int barValue = (int) (value * getMaximum());
        setValue(barValue);

        for(Listener changeListener : this.changeListeners) {
            changeListener.valueChanged(barValue);
        }
    }

}
