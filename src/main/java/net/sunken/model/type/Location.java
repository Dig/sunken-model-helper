package net.sunken.model.type;

import lombok.Getter;

public class Location {

    @Getter
    private double x;
    @Getter
    private double y;
    @Getter
    private double z;

    public Location (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
