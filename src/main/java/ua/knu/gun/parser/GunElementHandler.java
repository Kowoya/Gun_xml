package ua.knu.gun.parser;

import ua.knu.gun.model.FiringRange;
import ua.knu.gun.model.Gun;
import ua.knu.gun.model.Handy;
import ua.knu.gun.model.Material;
import ua.knu.gun.model.Origin;
import ua.knu.gun.model.Ttc;
import ua.knu.gun.model.XmlEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

/**
 * Builds {@link Gun} objects from a stream of "start element / text / end element" events.
 * <p>
 * SAX, StAX and DOM parsers only translate their own API calls into these three events,
 * so the logic of filling the objects is written once.
 */
public class GunElementHandler {

    private static final Logger LOGGER = Logger.getLogger(GunElementHandler.class.getName());

    private final List<Gun> guns = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();
    private Gun currentGun;
    private Ttc currentTtc;

    /**
     * Called when an element starts.
     *
     * @param localName  element name without prefix
     * @param attributes returns the value of an attribute by its name, or {@code null}
     */
    public void startElement(String localName, UnaryOperator<String> attributes) {
        text.setLength(0);
        Optional<GunXmlTag> tag = GunXmlTag.fromTagName(localName);
        if (tag.isEmpty()) {
            LOGGER.warning(() -> "Unknown element is skipped: " + localName);
            return;
        }
        if (tag.get() == GunXmlTag.FIREARM) {
            currentGun = new Gun();
            currentGun.setId(requireAttribute(attributes, GunXmlTag.ID_ATTRIBUTE));
            currentGun.setHandy(XmlEnum.fromXml(Handy.class,
                    requireAttribute(attributes, GunXmlTag.HANDY_ATTRIBUTE)));
        } else if (tag.get() == GunXmlTag.TTC) {
            currentTtc = new Ttc();
        }
    }

    /**
     * Called for text content of the current element (may be called several times).
     *
     * @param chunk part of the text
     */
    public void characters(String chunk) {
        text.append(chunk);
    }

    /**
     * Called when an element ends.
     *
     * @param localName element name without prefix
     */
    public void endElement(String localName) {
        String value = text.toString().strip();
        text.setLength(0);
        GunXmlTag.fromTagName(localName).ifPresent(tag -> applyValue(tag, value));
    }

    /**
     * @return all firearms read so far
     */
    public List<Gun> getGuns() {
        return guns;
    }

    private void applyValue(GunXmlTag tag, String value) {
        switch (tag) {
            case MODEL -> currentGun.setModel(value);
            case ORIGIN -> currentGun.setOrigin(XmlEnum.fromXml(Origin.class, value));
            case MATERIAL -> currentGun.setMaterial(XmlEnum.fromXml(Material.class, value));
            case FIRING_RANGE -> currentTtc.setFiringRange(XmlEnum.fromXml(FiringRange.class, value));
            case SIGHTING_RANGE -> currentTtc.setSightingRange(Integer.parseInt(value));
            case CLIP -> currentTtc.setClip(parseXmlBoolean(value));
            case OPTICS -> currentTtc.setOptics(parseXmlBoolean(value));
            case TTC -> currentGun.setTtc(currentTtc);
            case FIREARM -> {
                guns.add(currentGun);
                LOGGER.fine(() -> "Parsed " + currentGun);
            }
            default -> {
                // root element has no value of its own
            }
        }
    }

    private static String requireAttribute(UnaryOperator<String> attributes, String name) {
        String value = attributes.apply(name);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing required attribute: " + name);
        }
        return value;
    }

    /** xs:boolean allows "true", "false", "1" and "0". */
    static boolean parseXmlBoolean(String value) {
        if ("true".equals(value) || "1".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "0".equals(value)) {
            return false;
        }
        throw new IllegalArgumentException("Not an xs:boolean value: " + value);
    }
}
