package ua.knu.gun.model;

/**
 * Main material the weapon is made of.
 */
public enum Material implements XmlEnum {
    STEEL("steel"),
    ALUMINUM("aluminum"),
    POLYMER("polymer"),
    WOOD("wood"),
    TITANIUM("titanium");

    private final String xmlValue;

    Material(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    @Override
    public String xmlValue() {
        return xmlValue;
    }
}
