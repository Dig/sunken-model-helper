package net.sunken.model;

import net.sunken.model.component.ScrollingTextArea;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Helper extends JFrame {

    private JPanel container;

    public Helper() {
        container = new JPanel();
        container.setLayout(new GridLayout(1,2));

        this.display();
    }

    private void display() {
        setTitle(Constants.PROGRAM_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.add(new ScrollingTextArea(), BorderLayout.EAST);
        container.add(new ScrollingTextArea(), BorderLayout.WEST);

        add(container);
        pack();
        
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Helper();
    }
}
