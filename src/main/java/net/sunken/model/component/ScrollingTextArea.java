package net.sunken.model.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ScrollingTextArea extends JPanel {

    private JPanel scrollPanel;
    private JScrollPane scrollPane;

    public ScrollingTextArea(String title){
        setLayout(new BorderLayout());
        setBorder(null);

        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(0, 1, 3, 3));

        scrollPane = new JScrollPane(scrollPanel);

        add(new JLabel(title, SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new ImageButton("plus.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JTextArea area = new JTextArea();
                area.setLineWrap(true);
                area.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                scrollPanel.add(area);
                scrollPanel.revalidate();

                // Scroll to bottom
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
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

    public List<String> getTextList() {
        List<String> lines = new ArrayList<>();

        for (int i = 0; i < this.getTextAreaAmount(); i++){
            lines.add(this.getText(i));
        }

        return lines;
    }

    public int getTextAreaAmount() {
        return scrollPanel.getComponentCount();
    }

    public String getText(int index) {
        if (scrollPanel.getComponents().length >= (index + 1)) {
            JTextArea area = (JTextArea) scrollPanel.getComponent(index);
            return area.getText();
        }

        return null;
    }

}
