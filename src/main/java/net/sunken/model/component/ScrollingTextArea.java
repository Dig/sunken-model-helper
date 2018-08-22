package net.sunken.model.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ScrollingTextArea extends JPanel {

    private JPanel scrollPanel;

    public ScrollingTextArea(){
        setLayout(new BorderLayout());

        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(0, 1, 3, 3));

        add(new JScrollPane(scrollPanel), BorderLayout.CENTER);
        add(new JButton(new AbstractAction("Add") {

            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPanel.add(new JTextArea());
                scrollPanel.revalidate();
            }
        }), BorderLayout.SOUTH);
    }

}
