package net.sunken.model;

import com.google.gson.Gson;
import lombok.Getter;
import net.sunken.model.component.BigTextArea;
import net.sunken.model.component.ImageButton;
import net.sunken.model.component.ScrollingTextArea;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

public class Helper extends JFrame {

    @Getter
    private static Gson GSON = new Gson();

    private JPanel container;

    public Helper() {
        container = new JPanel();
        container.setLayout(new GridLayout(1,2));

        this.display();
    }

    private void display() {
        setTitle(Constants.PROGRAM_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.add(this.getMainComponent());
        container.add(new BigTextArea("Commands", 180, 235), BorderLayout.WEST);

        add(container);
        pack();

        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel getMainComponent() {
        JPanel panel = new JPanel();
        panel.setBorder(null);

        JLabel nameLabel = new JLabel("Name");
        JTextField name = new JTextField();
        name.setPreferredSize(new Dimension(180, 20));
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(nameLabel);
        panel.add(name);

        JLabel descriptionLabel = new JLabel("Description");
        JTextArea description = new JTextArea();
        description.setPreferredSize(new Dimension(180, 80));
        description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        description.setLineWrap(true);

        panel.add(descriptionLabel);
        panel.add(description);

        panel.add(new ImageButton("download.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String name = ((JTextField)((JPanel) container.getComponent(0)).getComponent(1)).getText();
                String description = ((JTextArea)((JPanel) container.getComponent(0)).getComponent(3)).getText();

                String commands = ((BigTextArea) container.getComponent(1)).getText();

                if (name != null && name.length() > 0 && commands.length() > 0) {
                    Generator generator = new Generator(name, description, commands);
                } else {
                    JOptionPane.showMessageDialog(null, "Must input at least 1 command and a name!");
                }
            }

        }, SwingConstants.CENTER), BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Helper();
            }
        });
    }
}
