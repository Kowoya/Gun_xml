package ua.knu.gun.parser;

import ua.knu.gun.exception.GunXmlException;
import ua.knu.gun.model.Gun;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common part of all parsers: file check, logging and error handling (template method).
 * Subclasses only feed the document into a {@link GunElementHandler}.
 */
public abstract class AbstractGunParser implements GunParser {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public final List<Gun> parse(Path xmlFile) throws GunXmlException {
        if (xmlFile == null || !Files.isRegularFile(xmlFile)) {
            throw new GunXmlException("XML file not found: " + xmlFile);
        }
        logger.info(() -> "Parsing " + xmlFile + " with " + getClass().getSimpleName());
        GunElementHandler handler = new GunElementHandler();
        try {
            doParse(xmlFile, handler);
        } catch (Exception e) {
            // each XML API throws its own checked exceptions; they all mean "cannot parse"
            logger.log(Level.SEVERE, "Cannot parse " + xmlFile, e);
            throw new GunXmlException("Cannot parse " + xmlFile + ": " + e.getMessage(), e);
        }
        List<Gun> guns = handler.getGuns();
        logger.info(() -> getClass().getSimpleName() + " read " + guns.size() + " firearms");
        return guns;
    }

    /**
     * Reads the document and reports its elements to the handler.
     *
     * @param xmlFile existing XML file
     * @param handler receiver of element events
     * @throws Exception any error of the underlying XML API
     */
    protected abstract void doParse(Path xmlFile, GunElementHandler handler) throws Exception;
}
