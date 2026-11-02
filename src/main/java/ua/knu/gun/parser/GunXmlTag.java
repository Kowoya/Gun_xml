package ua.knu.gun.parser;

import java.util.Arrays;
import java.util.Optional;

/**
 * Names of the elements and attributes of gun.xml.
 * Shared by all parsers so tag names are written in one place only.
 */
public enum GunXmlTag {
    GUN("Gun"),
    FIREARM("firearm"),
    MODEL("model"),
    ORIGIN("origin"),
    TTC("ttc"),
    FIRING_RANGE("firing-range"),
    SIGHTING_RANGE("sighting-range"),
    CLIP("clip"),
    OPTICS("optics"),
    MATERIAL("material");

    /** Attribute names of the {@code firearm} element. */
    public static final String ID_ATTRIBUTE = "id";
    public static final String HANDY_ATTRIBUTE = "handy";

    /** Namespace of gun.xsd. */
    public static final String NAMESPACE = "http://www.knu.ua/gun";

    private final String tagName;

    GunXmlTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * @param localName element name without namespace prefix
     * @return matching tag or empty if the element is unknown
     */
    public static Optional<GunXmlTag> fromTagName(String localName) {
        return Arrays.stream(values())
                .filter(tag -> tag.tagName.equals(localName))
                .findFirst();
    }
}
