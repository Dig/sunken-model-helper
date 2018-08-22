package net.sunken.model.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScrollingTextArea extends JPanel {

    private JPanel scrollPanel;

    public ScrollingTextArea(String title){
        setLayout(new BorderLayout());
        setBorder(null);

        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(0, 1, 3, 3));

        add(new JLabel(title, SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        add(new JScrollPane(scrollPanel), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new ImageButton("plus.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JTextArea area = new JTextArea();
                area.setLineWrap(true);
                area.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                scrollPanel.add(area);
                scrollPanel.revalidate();
            }

        }, SwingConstants.RIGHT));

        buttons.add(new ImageButton("minus.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (scrollPanel.getComponents().length > 0) {
                    scrollPanel.remove(scrollPanel.getComponents().length - 1);
                    scrollPanel.revalidate();
                }
            }

        }, SwingConstants.RIGHT));

        add(buttons, BorderLayout.SOUTH);

    }

}
