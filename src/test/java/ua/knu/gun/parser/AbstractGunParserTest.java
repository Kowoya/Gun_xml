package ua.knu.gun.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.knu.gun.TestData;
import ua.knu.gun.exception.GunXmlException;
import ua.knu.gun.model.Gun;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The same checks run for SAX, DOM and StAX: each subclass only supplies the parser.
 */
abstract class AbstractGunParserTest {

    private GunParser parser;

    protected abstract GunParser createParser();

    @BeforeEach
    void setUp() {
        parser = createParser();
    }

    @Test
    void readsAllFirearms() throws GunXmlException {
        assertEquals(TestData.GUNS_IN_XML, parser.parse(TestData.GUNS_XML).size());
    }

    @Test
    void fillsEveryFieldOfFirstAndLastFirearm() throws GunXmlException {
        List<Gun> guns = parser.parse(TestData.GUNS_XML);
        assertEquals(TestData.fort12(), guns.get(0));
        assertEquals(TestData.berettaArx160(), guns.get(guns.size() - 1));
    }

    @Test
    void keepsDocumentOrder() throws GunXmlException {
        List<Gun> guns = parser.parse(TestData.GUNS_XML);
        for (int i = 0; i < guns.size(); i++) {
            assertEquals("g" + (i + 1), guns.get(i).getId());
        }
    }

    @Test
    void handlesCdataUnknownElementsAndNumericBooleans() throws GunXmlException {
        List<Gun> guns = parser.parse(TestData.EXTRA_XML);
        assertEquals(List.of(TestData.fort17()), guns);
    }

    @Test
    void missingFileIsReported() {
        GunXmlException e = assertThrows(GunXmlException.class, () -> parser.parse(TestData.MISSING_FILE));
        assertTrue(e.getMessage().contains("not found"));
    }

    @Test
    void nullPathIsReported() {
        assertThrows(GunXmlException.class, () -> parser.parse(null));
    }

    @Test
    void malformedXmlIsReported() {
        GunXmlException e = assertThrows(GunXmlException.class, () -> parser.parse(TestData.MALFORMED_XML));
        assertNotNull(e.getCause());
    }

    @Test
    void unknownAttributeValueIsReported() {
        GunXmlException e = assertThrows(GunXmlException.class, () -> parser.parse(TestData.BAD_HANDY_XML));
        assertTrue(e.getCause() instanceof IllegalArgumentException);
        assertTrue(e.getMessage().contains("three-handed"));
    }
}
