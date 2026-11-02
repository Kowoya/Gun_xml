package ua.knu.gun.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlEnumTest {

    @Test
    void everyConstantIsFoundByItsXmlValue() {
        assertRoundTrip(Handy.class);
        assertRoundTrip(Origin.class);
        assertRoundTrip(Material.class);
        assertRoundTrip(FiringRange.class);
    }

    @Test
    void xmlValuesMatchSchema() {
        assertEquals("one-handed", Handy.ONE_HANDED.xmlValue());
        assertEquals("USA", Origin.USA.xmlValue());
        assertEquals("aluminum", Material.ALUMINUM.xmlValue());
        assertEquals("far", FiringRange.FAR.xmlValue());
    }

    @Test
    void unknownValueIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> XmlEnum.fromXml(Origin.class, "Atlantis"));
        assertTrue(e.getMessage().contains("Origin"));
        assertTrue(e.getMessage().contains("Atlantis"));
    }

    @Test
    void lookupIsCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> XmlEnum.fromXml(Material.class, "STEEL"));
    }

    private static <E extends Enum<E> & XmlEnum> void assertRoundTrip(Class<E> type) {
        for (E constant : type.getEnumConstants()) {
            assertEquals(constant, XmlEnum.fromXml(type, constant.xmlValue()));
        }
    }
}
