package net.sunken.model.component;

import javax.swing.*;
import java.awt.*;

public class BigTextArea extends JPanel {

    public BigTextArea(String title, int width, int height) {

        add(new JLabel(title, SwingConstants.HORIZONTAL), BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        area.setPreferredSize(new Dimension(width, height));

        add(area);
    }

    public String getText(){
        return ((JTextArea) this.getComponent(1)).getText();
    }

}
