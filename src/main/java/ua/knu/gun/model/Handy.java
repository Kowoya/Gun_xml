package ua.knu.gun.model;

/**
 * One- or two-handed weapon.
 */
public enum Handy implements XmlEnum {
    ONE_HANDED("one-handed"),
    TWO_HANDED("two-handed");

    private final String xmlValue;

    Handy(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    @Override
    public String xmlValue() {
        return xmlValue;
    }
}
