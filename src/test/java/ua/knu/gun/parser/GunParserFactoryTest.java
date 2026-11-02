package ua.knu.gun.parser;

import org.junit.jupiter.api.Test;
import ua.knu.gun.TestData;
import ua.knu.gun.exception.GunXmlException;
import ua.knu.gun.model.Gun;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunParserFactoryTest {

    @Test
    void createsParserOfRequestedType() {
        assertTrue(GunParserFactory.create(ParserType.SAX) instanceof SaxGunParser);
        assertTrue(GunParserFactory.create(ParserType.DOM) instanceof DomGunParser);
        assertTrue(GunParserFactory.create(ParserType.STAX) instanceof StaxGunParser);
    }

    @Test
    void allParsersProduceTheSameCollection() throws GunXmlException {
        List<Gun> expected = GunParserFactory.create(ParserType.DOM).parse(TestData.GUNS_XML);
        for (ParserType type : ParserType.values()) {
            assertEquals(expected, GunParserFactory.create(type).parse(TestData.GUNS_XML), type.name());
        }
    }
}
