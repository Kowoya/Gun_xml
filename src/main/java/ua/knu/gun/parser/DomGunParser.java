package ua.knu.gun.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;

/**
 * Tree-based parser built on DOM: the whole document is loaded into memory,
 * then the tree is walked recursively.
 */
public class DomGunParser extends AbstractGunParser {

    @Override
    protected void doParse(Path xmlFile, GunElementHandler handler) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Document document = factory.newDocumentBuilder().parse(xmlFile.toFile());
        walk(document.getDocumentElement(), handler);
    }

    private static void walk(Element element, GunElementHandler handler) {
        handler.startElement(element.getLocalName(),
                name -> element.hasAttribute(name) ? element.getAttribute(name) : null);
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                walk((Element) child, handler);
            } else if (child instanceof Text) {
                handler.characters(((Text) child).getData());
            }
        }
        handler.endElement(element.getLocalName());
    }
}
