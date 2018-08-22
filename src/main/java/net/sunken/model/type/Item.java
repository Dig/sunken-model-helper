package net.sunken.model.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.ToString;
import net.sunken.model.Helper;
import net.sunken.model.exception.ItemParseException;

import java.io.Serializable;

@ToString
public class Item {

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

        if (args[2].contains("~") || args[3].contains("~") || args[4].contains("~")) {
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
    }
}
