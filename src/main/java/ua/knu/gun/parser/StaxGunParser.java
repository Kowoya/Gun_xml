package ua.knu.gun.parser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Event-based parser built on StAX (pull model): the program asks the reader for the next event.
 */
public class StaxGunParser extends AbstractGunParser {

    @Override
    protected void doParse(Path xmlFile, GunElementHandler handler) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        try (InputStream input = Files.newInputStream(xmlFile)) {
            XMLStreamReader reader = factory.createXMLStreamReader(input);
            try {
                while (reader.hasNext()) {
                    switch (reader.next()) {
                        case XMLStreamConstants.START_ELEMENT ->
                                handler.startElement(reader.getLocalName(),
                                        name -> reader.getAttributeValue(null, name));
                        case XMLStreamConstants.CHARACTERS, XMLStreamConstants.CDATA ->
                                handler.characters(reader.getText());
                        case XMLStreamConstants.END_ELEMENT -> handler.endElement(reader.getLocalName());
                        default -> {
                            // comments, processing instructions, whitespace outside elements
                        }
                    }
                }
            } finally {
                reader.close();
            }
        }
    }
}
