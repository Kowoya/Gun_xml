package ua.knu.gun.parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import java.nio.file.Path;

/**
 * Event-based parser built on SAX (push model).
 */
public class SaxGunParser extends AbstractGunParser {

    @Override
    protected void doParse(Path xmlFile, GunElementHandler handler) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.newSAXParser().parse(xmlFile.toFile(), new SaxAdapter(handler));
    }

    /** Translates SAX callbacks into {@link GunElementHandler} events. */
    private static final class SaxAdapter extends DefaultHandler {

        private final GunElementHandler handler;

        private SaxAdapter(GunElementHandler handler) {
            this.handler = handler;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            handler.startElement(localName, attributes::getValue);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            handler.characters(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            handler.endElement(localName);
        }
    }
}
