package fr.redstonneur1256.redutilities.graphics.swing;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

public class PlaceHolderTextField extends JTextField {

    private String placeHolder;

    public PlaceHolderTextField() {
    }

    public PlaceHolderTextField(String text, String placeHolder) {
        super(text);
        this.placeHolder = placeHolder;
    }

    public PlaceHolderTextField(int columns) {
        super(columns);
    }

    public PlaceHolderTextField(String text, int columns) {
        super(text, columns);
    }

    public PlaceHolderTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(placeHolder != null && (getText() == null || getText().isEmpty())) {
            g.setColor(Color.GRAY);
            g.drawString(placeHolder, 3, g.getFontMetrics().getAscent());
        }
    }
}