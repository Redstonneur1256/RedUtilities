package fr.redstonneur1256.redutilities.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowMover extends MouseAdapter {
    private JFrame frame;
    private int x, y;
    public WindowMover(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(x == -1)
            return;

        Point location = MouseInfo.getPointerInfo().getLocation();

        frame.setLocation(location.x - x, location.y - y);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public void apply() {
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
    }
}
