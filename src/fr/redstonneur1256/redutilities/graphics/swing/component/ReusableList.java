package fr.redstonneur1256.redutilities.graphics.swing.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class like javax.swing.JList but the components returned by the {@code CellRenderer} can be edited
 *
 * @param <T> the type of the elements of the list
 */
public class ReusableList<T> extends JComponent implements MouseListener {

    private final List<Listener<T>> listeners;
    private CellRenderer<T> renderer;
    private T[] items;
    private JComponent[] components;

    public ReusableList() {
        this(new DefaultCellRenderer<>());
    }

    public ReusableList(CellRenderer<T> renderer) {
        this.listeners = new ArrayList<>();
        this.renderer = renderer;
        this.items = null;
        this.components = new JComponent[0];

        this.setLayout(null);

        this.addMouseListener(this);
    }

    public void updateComponents() {
        removeAll();
        for(int i = 0; i < items.length; i++) {
            invalidateComponent0(i);
        }
        recalculateBounds();
        repaint();
    }

    public void invalidateComponent(int index) {
        invalidateComponent0(index);
        recalculateBounds();
        repaint();
    }

    private void invalidateComponent0(int index) {
        JComponent oldComponent = components[index];
        JComponent newComponent = renderer.render(this, items[index], index);

        if(oldComponent != null) {
            remove(oldComponent);
        }

        components[index] = newComponent;

        int y = 0;
        for(int i = 0; i < index; i++) {
            JComponent component = components[i];
            y += component == null ? 0 : component.getPreferredSize().getHeight();
        }

        Dimension size = newComponent.getPreferredSize();

        newComponent.setBounds(0, y, size.width, size.height);
        add(newComponent);

    }

    public void recalculateBounds() {
        int width = 0;
        int height = 0;

        for(JComponent component : components) {
            if(component == null) {
                continue;
            }
            Dimension size = component.getPreferredSize();

            width = (int) Math.max(size.getWidth(), width);
            height += size.getHeight();
        }

        setPreferredSize(new Dimension(width, height));
    }

    public CellRenderer<T> getRenderer() {
        return renderer;
    }

    public void setRenderer(CellRenderer<T> renderer) {
        Objects.requireNonNull(renderer, "renderer cannot be null");
        this.renderer = renderer;
    }

    public T[] getItems() {
        return items;
    }

    public void setItems(T[] items) {
        this.items = items;
        this.components = new JComponent[items.length];
        this.updateComponents();
    }

    public void addListener(Listener<T> listener) {
        Objects.requireNonNull(listener, "listener cannot be null");
        synchronized(listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(Listener<T> listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }

    public List<Listener<T>> getListeners() {
        synchronized(listeners) {
            return listeners;
        }
    }

    public JComponent[] getDrawComponents() {
        return components.clone();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int clicks = e.getClickCount();

        for(int i = 0; i < components.length; i++) {
            JComponent component = components[i];

            if(component != null && component.getBounds().contains(x, y)) {
                synchronized(listeners) {
                    for(Listener<T> listener : listeners) {
                        listener.onPressed(items[i], clicks);
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public interface Listener<T> {

        void onPressed(T item, int clicks);

    }

    public interface CellRenderer<T> {

        JComponent render(ReusableList<T> parent, T value, int index);

    }

    public static class DefaultCellRenderer<T> implements CellRenderer<T> {

        @Override
        public JComponent render(ReusableList<T> parent, T value, int index) {
            JLabel label;
            if(value instanceof ImageIcon) {
                label = new JLabel((ImageIcon) value);
            }else {
                label = new JLabel(String.valueOf(value));
                label.setFont(parent.getFont());
            }
            label.setComponentOrientation(parent.getComponentOrientation());
            label.setOpaque(parent.isOpaque());
            label.setForeground(parent.getForeground());
            label.setBackground(parent.getBackground());
            return label;
        }

    }

}
