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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Location other = (Location) obj;

        return com.google.common.base.Objects.equal(this.x, other.getX())
                && com.google.common.base.Objects.equal(this.y, other.getY())
                && com.google.common.base.Objects.equal(this.z, other.getZ());
    }
}
