package ua.knu.gun.model;

/**
 * Country of manufacture.
 */
public enum Origin implements XmlEnum {
    UKRAINE("Ukraine"),
    USA("USA"),
    GERMANY("Germany"),
    BELGIUM("Belgium"),
    AUSTRIA("Austria"),
    ISRAEL("Israel"),
    ITALY("Italy");

    private final String xmlValue;

    Origin(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    @Override
    public String xmlValue() {
        return xmlValue;
    }
}
