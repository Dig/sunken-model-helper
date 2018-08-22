package net.sunken.model.component;

import javax.swing.*;
import java.awt.event.MouseAdapter;

public class ImageButton extends JLabel {

    private ImageIcon icon;

    public ImageButton (String path, MouseAdapter action, int alignment){
        setIcon(this.createImageIcon(path));

        setOpaque(false);
        setHorizontalAlignment(alignment);
        addMouseListener(action);
    }

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource("/" + path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            return icon;
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
