package ua.knu.gun.model;

import java.util.Objects;

/**
 * A firearm described by the {@code firearm} element of gun.xsd.
 */
public class Gun {

    private String id;
    private Handy handy;
    private String model;
    private Origin origin;
    private Ttc ttc;
    private Material material;

    public Gun() {
    }

    public Gun(String id, Handy handy, String model, Origin origin, Ttc ttc, Material material) {
        this.id = id;
        this.handy = handy;
        this.model = model;
        this.origin = origin;
        this.ttc = ttc;
        this.material = material;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Handy getHandy() {
        return handy;
    }

    public void setHandy(Handy handy) {
        this.handy = handy;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Ttc getTtc() {
        return ttc;
    }

    public void setTtc(Ttc ttc) {
        this.ttc = ttc;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gun)) {
            return false;
        }
        Gun other = (Gun) o;
        return Objects.equals(id, other.id)
                && handy == other.handy
                && Objects.equals(model, other.model)
                && origin == other.origin
                && Objects.equals(ttc, other.ttc)
                && material == other.material;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, handy, model, origin, ttc, material);
    }

    @Override
    public String toString() {
        return "Gun{id='" + id + '\''
                + ", handy=" + handy
                + ", model='" + model + '\''
                + ", origin=" + origin
                + ", ttc=" + ttc
                + ", material=" + material + '}';
    }
}
