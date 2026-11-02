package ua.knu.gun.parser;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunXmlTagTest {

    @Test
    void findsTagByName() {
        for (GunXmlTag tag : GunXmlTag.values()) {
            assertEquals(Optional.of(tag), GunXmlTag.fromTagName(tag.getTagName()));
        }
        assertEquals(Optional.of(GunXmlTag.SIGHTING_RANGE), GunXmlTag.fromTagName("sighting-range"));
    }

    @Test
    void unknownNameGivesEmptyResult() {
        assertTrue(GunXmlTag.fromTagName("note").isEmpty());
    }
}
