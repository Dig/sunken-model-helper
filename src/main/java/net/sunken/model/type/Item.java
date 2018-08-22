package net.sunken.model.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.ToString;
import net.sunken.model.Helper;
import net.sunken.model.exception.ItemParseException;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

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
    private double x;
    @Getter
    private double y;
    @Getter
    private double z;

    public Item(String command) throws ItemParseException {
        String[] args = command.split(" ");
        status = 0;

        if (args.length == 6) {
            if (args[2].contains("~") || args[3].contains("~") || args[4].contains("~")) {
                status = 1;
                throw new ItemParseException("Absolute coordinates must be enabled before creating the model!");
            }

            x = Double.parseDouble(args[2]);
            y = Double.parseDouble(args[3]);
            z = Double.parseDouble(args[4]);

            JsonObject jsonObj = Helper.getGSON().fromJson(args[5], JsonObject.class);

            size = ItemSize.LARGE;
            if (jsonObj.has("Small")
                    && jsonObj.get("Small").getAsInt() == 1) {
                size = ItemSize.MEDIUM;
            }

            visible = true;
            if (jsonObj.has("Invisible")
                    && jsonObj.get("Invisible").getAsInt() == 1) {
                visible = false;
            }

            JsonArray armor = jsonObj.get("ArmorItems").getAsJsonArray();

            JsonObject head = armor.get(armor.size() - 1).getAsJsonObject();
            material = head.get("id").getAsString();

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

            structFile.set("location.x", this.x);
            structFile.set("location.y", this.y);
            structFile.set("location.z", this.z);

            structFile.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
