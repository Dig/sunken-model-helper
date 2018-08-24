package net.sunken.model.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.ToString;
import net.sunken.model.Constants;
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
    private String uuid;
    @Getter
    private String material;
    @Getter
    private ItemSize size;
    @Getter
    private boolean visible;

    @Getter
    private Location location;
    @Getter
    private Rotation rotation;

    @Getter
    private Map<String, Location> pose;

    public Item(String uuid, String material, ItemSize size, boolean visible,
                Location location, Rotation rotation, Map<String, Location> pose) {
        this.uuid = uuid;
        this.material = material;
        this.size = size;
        this.visible = visible;
        this.location = location;
        this.rotation = rotation;
        this.pose = pose;

        this.status = 2;
    }

    public Item(JsonObject jsonObj) throws ItemParseException {
        JsonObject nbt = jsonObj.get("nbt").getAsJsonObject();
        status = 0;

        location = new Location(jsonObj.get("x").getAsDouble(),
                jsonObj.get("y").getAsDouble(),
                jsonObj.get("z").getAsDouble());

        uuid = jsonObj.get("uuid").getAsString();

        size = ItemSize.LARGE;
        if (nbt.has("Small")
                && nbt.get("Small").getAsInt() == 1) {
            size = ItemSize.MEDIUM;
        }

        String entity = jsonObj.get("entity").getAsString();
        if (entity.equalsIgnoreCase("villager")) {
            size = ItemSize.SMALL;
        }

        visible = true;
        if (nbt.has("Invisible")
                && nbt.get("Invisible").getAsInt() == 1) {
            visible = false;
        }

        rotation = new Rotation(0, 0);
        if (nbt.has("Rotation")) {
            JsonArray array = nbt.getAsJsonArray("Rotation");
            rotation = new Rotation(array.get(0).getAsFloat(), array.get(1).getAsFloat());
        }

        this.pose = new HashMap<>();
        if (entity.equalsIgnoreCase("armor_stand") && nbt.has("Pose")) {
            JsonObject poses = nbt.get("Pose").getAsJsonObject();

            for (String pose : Constants.POSES) {
                if (poses.has(pose)) {
                    JsonArray arr = poses.get(pose).getAsJsonArray();

                    this.pose.put(pose, new Location(arr.get(0).getAsFloat(),
                            arr.get(1).getAsFloat(), arr.get(2).getAsFloat()));
                }
            }
        }

        material = "AIR";
        if (nbt.has("ArmorItems")) {
            JsonArray armor = nbt.get("ArmorItems").getAsJsonArray();
            JsonObject head = armor.get(armor.size() - 1).getAsJsonObject();
            material = head.get("id").getAsString();
        }

        status = 2;
    }

    public boolean saveFile(String path) {
        String fileName = this.uuid + ".yml";
        YamlFile structFile = new YamlFile(path + File.separator + fileName);

        try {
            structFile.createNewFile(true);

            structFile.set("material", this.material);
            structFile.set("size", this.size.toString());
            structFile.set("visible", this.visible);

            structFile.set("location.x", this.location.getX());
            structFile.set("location.y", this.location.getY());
            structFile.set("location.z", this.location.getZ());

            structFile.set("rotation.x", this.rotation.getX());
            structFile.set("rotation.y", this.rotation.getY());

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Item other = (Item) obj;

        return com.google.common.base.Objects.equal(this.uuid, other.getUuid())
                && com.google.common.base.Objects.equal(this.material, other.getMaterial())
                && com.google.common.base.Objects.equal(this.size, other.getSize())
                && com.google.common.base.Objects.equal(this.visible, other.isVisible())
                && this.location.equals(other.getLocation())
                && com.google.common.base.Objects.equal(this.pose, other.getPose())
                && this.rotation.equals(other.getRotation());
    }
}
