package net.sunken.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sunken.model.exception.ItemParseException;
import net.sunken.model.type.Item;
import net.sunken.model.type.ItemSize;
import net.sunken.model.type.Location;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.Yaml;
import org.zeroturnaround.zip.ZipInfoCallback;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Generator {

    private List<Item> items;

    public Generator (String name, String description, String allCmds) {
        this.items = new ArrayList<>();

        JsonArray array = Helper.getGSON().fromJson(allCmds, JsonArray.class);
        for (int i = 0; i < array.size(); i++) {
            JsonObject jsonObj = array.get(i).getAsJsonObject();

            try {
                Item item = new Item(jsonObj);

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

    public Generator(File file, String name, List<String> commands) {
        Map<Integer, List<Item>> frames = new HashMap<>();

        this.items = new ArrayList<>();
        int frameNum = 1;
        List<Item> prevItems = new ArrayList<>();

        // Open the file
        UUID uuid = UUID.randomUUID();
        File folder = new File(file.getParent() + File.separator + uuid.toString());
        ZipUtil.unpack(file, folder);

        if (folder.isDirectory()) {
            File structure = new File(folder.getAbsolutePath() + File.separator + "structure");

            for (File child : structure.listFiles()) {
                if (child.getName().endsWith(".yml")) {
                    System.out.println("Loading: " + child.getName());
                    Item loaded = this.loadItem(child);
                    prevItems.add(loaded);
                }
            }
        }

        // Load animation commands
        for (String frame : commands) {
            JsonArray array = Helper.getGSON().fromJson(frame, JsonArray.class);

            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObj = array.get(i).getAsJsonObject();

                try {
                    Item item = new Item(jsonObj);

                    if (item.getStatus() == 2) {
                        items.add(item);
                    }
                } catch (ItemParseException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Items: " + this.items.toString());
            System.out.println("Prev Items: " + prevItems.toString());

            System.out.println("Items: " + this.items.size());
            System.out.println("Prev Items: " + prevItems.size());

            for (Item item : this.items) {
                for (Item other : prevItems) {
                    if (item.getUuid().equals(other.getUuid())  && !item.equals(other)) {
                        System.out.println("Saving file " + item.getUuid());

                        File animationDir = new File(folder.getAbsolutePath() + File.separator + "animation");
                        File typeDir = new File(animationDir.getAbsolutePath() + File.separator + name);
                        File frameNumDir = new File(typeDir.getAbsolutePath() + File.separator + frameNum);

                        if (!animationDir.exists()) { animationDir.mkdir(); }
                        if (!typeDir.exists()) { typeDir.mkdir(); }
                        if (!frameNumDir.exists()) { frameNumDir.mkdir(); }

                        item.saveFile(frameNumDir.getAbsolutePath());
                    }
                }
            }

            prevItems.clear();
            prevItems.addAll(this.items);
            this.items.clear();
            frameNum++;
        }

        try {
            ZipUtil.pack(folder, file);
            FileUtils.deleteDirectory(folder);

            JOptionPane.showMessageDialog(null, "Animation added to model!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Item loadItem(File file) {
        YamlFile structFile = new YamlFile(file.getAbsolutePath());

        try {
            structFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String material = structFile.getString("material");
        ItemSize size = ItemSize.valueOf(structFile.getString("size"));
        boolean visible = structFile.getBoolean("visible");

        Location location = new Location(structFile.getDouble("location.x"),
                structFile.getDouble("location.y"), structFile.getDouble("location.z"));

        Map<String, Location> pose = new HashMap<>();
        if (structFile.contains("pose")) {
            ConfigurationSection poses = structFile.getConfigurationSection("pose");

            for (String key : poses.getKeys(false)) {
                pose.put(key, new Location(
                        structFile.getDouble("pose." + key + ".x"),
                        structFile.getDouble("pose." + key + ".y"),
                        structFile.getDouble("pose." + key + ".z")
                ));
            }
        }

        return new Item(file.getName().replaceAll(".yml", ""),
                material, size, visible, location, pose);
    }

}
