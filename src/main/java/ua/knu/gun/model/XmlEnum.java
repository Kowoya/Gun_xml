package ua.knu.gun.model;

/**
 * An enum whose constants are written to XML as a text value
 * different from the Java constant name (e.g. {@code ONE_HANDED -> "one-handed"}).
 */
public interface XmlEnum {

    /**
     * @return the value used in the XML document
     */
    String xmlValue();

    /**
     * Finds the enum constant that corresponds to an XML value.
     *
     * @param type  enum class
     * @param value value from the XML document
     * @param <E>   enum type
     * @return matching constant
     * @throws IllegalArgumentException if no constant has this value
     */
    static <E extends Enum<E> & XmlEnum> E fromXml(Class<E> type, String value) {
        for (E constant : type.getEnumConstants()) {
            if (constant.xmlValue().equals(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Unknown " + type.getSimpleName() + " value: " + value);
    }
}
