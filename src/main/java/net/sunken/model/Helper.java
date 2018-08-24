package net.sunken.model;

import com.google.gson.Gson;
import lombok.Getter;
import net.sunken.model.component.ImageButton;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Helper extends JFrame {

    @Getter
    private static Gson GSON = new Gson();

    private JPanel container;

    public Helper() {
        container = new JPanel();
        container.setLayout(new GridLayout(1,1));

        this.display();
    }

    private void display() {
        setTitle(Constants.PROGRAM_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        main.add(new ImageButton("plus.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                new Create();
            }

        }, SwingConstants.CENTER));

        main.add(new ImageButton("fast-forward.png", new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                FileNameExtensionFilter filter = new FileNameExtensionFilter("SUNKEN FILES", "sunken");
                jfc.setFileFilter(filter);
                jfc.setDialogTitle("Open Sunken Model");

                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    new Animation(selectedFile);
                }
            }

        }, SwingConstants.CENTER));

        container.add(main);

        add(container);
        pack();

        setSize(190, 70);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Helper();
            }
        });
    }
}
