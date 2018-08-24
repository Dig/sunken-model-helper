package net.sunken.model;

import net.sunken.model.component.ImageButton;
import net.sunken.model.component.ScrollingTextArea;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Animation extends JFrame {

    private File file;
    private JPanel container;

    public Animation(File file) {
        this.file = file;

        container = new JPanel();
        container.setLayout(new GridLayout(1,2));

        this.display();
    }

    private void display() {
        setTitle(Constants.PROGRAM_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        container.add(this.getMainComponent());
        container.add(new ScrollingTextArea("Animation Commands"));

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

        panel.add(new ImageButton("fast-forward.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String name = ((JTextField)((JPanel) container.getComponent(0)).getComponent(1)).getText();

                List<String> commands = ((ScrollingTextArea) container.getComponent(1)).getTextList();

                if (name != null && name.length() > 0 && commands.size() > 0) {
                    Generator generator = new Generator(file, name, commands);
                } else {
                    JOptionPane.showMessageDialog(null, "Must input at least 1 command and a name!");
                }
            }

        }, SwingConstants.CENTER), BorderLayout.SOUTH);

        return panel;
    }

}
