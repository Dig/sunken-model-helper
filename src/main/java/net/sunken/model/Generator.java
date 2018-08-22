package net.sunken.model;

import net.sunken.model.exception.ItemParseException;
import net.sunken.model.type.Item;

import java.util.ArrayList;
import java.util.List;

public class Generator {

    private List<String> commands;
    private List<Item> items;

    public Generator (List<String> commands) {
        this.commands = commands;
        this.items = new ArrayList<>();

        for (String command : this.commands) {
            try {
                items.add(new Item(command));
            } catch (ItemParseException e) {
                e.printStackTrace();
            }
        }

        
    }

}
