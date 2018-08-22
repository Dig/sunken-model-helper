package net.sunken.model;

import net.sunken.model.exception.ItemParseException;
import net.sunken.model.type.Item;
import org.simpleyaml.configuration.file.YamlFile;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Generator {

    private List<String> commands;
    private List<Item> items;

    public Generator (String name, String description, List<String> commands) {
        this.commands = commands;
        this.items = new ArrayList<>();

        for (String command : this.commands) {
            try {
                Item item = new Item(command);

                if (item.getStatus() == 2) {
                    items.add(item);
                }
            } catch (ItemParseException e) {
                e.printStackTrace();
            }
        }

        if (this.items.size() > 0) {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setSelectedFile(new File(name + ".sunken"));
            int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                String baseDir = selectedFile.getAbsolutePath();

                // Create temp folder
                UUID uuid = UUID.randomUUID();
                File tempFolder = new File(selectedFile.getParent() + File.separator + uuid.toString());
                if (!tempFolder.exists()) {
                    tempFolder.mkdirs();
                }

                // Create structure folder
                File structureFolder = new File(tempFolder.getAbsolutePath() + File.separator + "structure");
                if (!structureFolder.exists()) {
                    structureFolder.mkdirs();
                }

                // Setup main config file
                YamlFile modelFile = new YamlFile(tempFolder.getAbsolutePath() + File.separator + "model.yml");
                try {
                    modelFile.createNewFile(true);

                    modelFile.set("name", name);
                    modelFile.set("description", description);
                    modelFile.set("created", System.currentTimeMillis());

                    modelFile.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Setup structure files
                for (Item item : this.items) {
                    item.saveFile(structureFolder.getAbsolutePath());
                }

                try {
                    ZipUtil.pack(new File(tempFolder.getAbsolutePath()), new File(baseDir));
                    FileUtils.deleteDirectory(new File(tempFolder.getAbsolutePath()));

                    JOptionPane.showMessageDialog(null, "Model creation complete!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid location for file to be saved.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Must have at least one valid item!");
        }
    }

}
