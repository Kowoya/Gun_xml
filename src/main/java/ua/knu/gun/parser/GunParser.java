package ua.knu.gun.parser;

import ua.knu.gun.exception.GunXmlException;
import ua.knu.gun.model.Gun;

import java.nio.file.Path;
import java.util.List;

/**
 * Reads a list of firearms from an XML document.
 */
public interface GunParser {

    /**
     * @param xmlFile path to the XML document
     * @return firearms in document order
     * @throws GunXmlException if the file is missing or cannot be parsed
     */
    List<Gun> parse(Path xmlFile) throws GunXmlException;
}
