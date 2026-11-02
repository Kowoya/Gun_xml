package ua.knu.gun.parser;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunElementHandlerTest {

    @Test
    void parsesXsBooleanValues() {
        assertTrue(GunElementHandler.parseXmlBoolean("true"));
        assertTrue(GunElementHandler.parseXmlBoolean("1"));
        assertFalse(GunElementHandler.parseXmlBoolean("false"));
        assertFalse(GunElementHandler.parseXmlBoolean("0"));
        assertThrows(IllegalArgumentException.class, () -> GunElementHandler.parseXmlBoolean("yes"));
    }

    @Test
    void missingIdIsRejected() {
        GunElementHandler handler = new GunElementHandler();
        Map<String, String> attributes = Map.of("handy", "one-handed");
        assertThrows(IllegalArgumentException.class,
                () -> handler.startElement("firearm", attributes::get));
    }

    @Test
    void emptyHandyIsRejected() {
        GunElementHandler handler = new GunElementHandler();
        Map<String, String> attributes = Map.of("id", "g1", "handy", "");
        assertThrows(IllegalArgumentException.class,
                () -> handler.startElement("firearm", attributes::get));
    }

    @Test
    void textSplitIntoChunksIsJoined() {
        GunElementHandler handler = new GunElementHandler();
        Map<String, String> attributes = Map.of("id", "g1", "handy", "two-handed");
        handler.startElement("Gun", attributes::get);
        handler.startElement("firearm", attributes::get);
        handler.startElement("model", attributes::get);
        handler.characters("HK ");
        handler.characters("G36");
        handler.endElement("model");
        handler.endElement("firearm");
        handler.endElement("Gun");

        assertEquals(1, handler.getGuns().size());
        assertEquals("HK G36", handler.getGuns().get(0).getModel());
    }
}
