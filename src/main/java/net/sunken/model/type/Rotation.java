package net.sunken.model.type;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Rotation {

    @Getter
    private float x;
    @Getter
    private float y;

    public Rotation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Rotation other = (Rotation) obj;

        return com.google.common.base.Objects.equal(this.x, other.getX())
                && com.google.common.base.Objects.equal(this.y, other.getY());
    }
}
