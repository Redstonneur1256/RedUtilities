package fr.redstonneur1256.redutilities.graphics.swing.component.abstractComp;

import javax.swing.*;

public class AbstractLabel extends JComponent {

    private String text;

    public AbstractLabel() {
    }

    public AbstractLabel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
