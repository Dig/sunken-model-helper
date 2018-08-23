package net.sunken.model.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.ToString;
import net.sunken.model.Constants;
import net.sunken.model.Helper;
import net.sunken.model.exception.ItemParseException;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@ToString
public class Item {

    @Getter
    private int status;

    @Getter
    private String material;
    @Getter
    private ItemSize size;
    @Getter
    private boolean visible;

    @Getter
    private Location location;

    @Getter
    private Map<String, Location> pose;

    public Item(String command) throws ItemParseException {
        String[] args = command.split(" ");
        status = 0;

        if (args.length == 6) {
            if (args[2].contains("~") || args[3].contains("~") || args[4].contains("~")) {
                status = 1;
                throw new ItemParseException("Absolute coordinates must be enabled before creating the model!");
            }

            location = new Location(Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));

            JsonObject jsonObj = Helper.getGSON().fromJson(args[5], JsonObject.class);

            size = ItemSize.LARGE;
            if (jsonObj.has("Small")
                    && jsonObj.get("Small").getAsInt() == 1) {
                size = ItemSize.MEDIUM;
            }
            if (args[1].equalsIgnoreCase("villager")) {
                size = ItemSize.SMALL;
            }

            visible = true;
            if (jsonObj.has("Invisible")
                    && jsonObj.get("Invisible").getAsInt() == 1) {
                visible = false;
            }

            this.pose = new HashMap<>();
            if (args[1].equalsIgnoreCase("armor_stand") && jsonObj.has("Pose")) {
                JsonObject poses = jsonObj.get("Pose").getAsJsonObject();

                for (String pose : Constants.POSES) {
                    if (poses.has(pose)) {
                        JsonArray arr = poses.get(pose).getAsJsonArray();

                        this.pose.put(pose, new Location(arr.get(0).getAsFloat(),
                                arr.get(1).getAsFloat(), arr.get(2).getAsFloat()));
                    }
                }
            }

            material = "AIR";
            if (jsonObj.has("ArmorItems")) {
                JsonArray armor = jsonObj.get("ArmorItems").getAsJsonArray();
                JsonObject head = armor.get(armor.size() - 1).getAsJsonObject();
                material = head.get("id").getAsString();
            }

            status = 2;
        } else {
            status = 1;
        }
    }

    public boolean saveFile(String path) {
        String fileName = UUID.randomUUID().toString() + ".yml";
        YamlFile structFile = new YamlFile(path + File.separator + fileName);

        try {
            structFile.createNewFile(true);

            structFile.set("material", this.material);
            structFile.set("size", this.size.toString());
            structFile.set("visible", this.visible);

            structFile.set("location.x", this.location.getX());
            structFile.set("location.y", this.location.getY());
            structFile.set("location.z", this.location.getZ());

            if (this.pose.size() > 0) {
                for (String key : this.pose.keySet()) {
                    Location loc = this.pose.get(key);

                    structFile.set("pose." + key + ".x", loc.getX());
                    structFile.set("pose." + key + ".y", loc.getY());
                    structFile.set("pose." + key + ".z", loc.getZ());
                }
            }

            structFile.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
