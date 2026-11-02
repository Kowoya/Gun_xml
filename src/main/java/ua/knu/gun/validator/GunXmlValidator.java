package ua.knu.gun.validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ua.knu.gun.exception.GunXmlException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Checks an XML document against an XSD schema.
 */
public class GunXmlValidator {

    private static final Logger LOGGER = Logger.getLogger(GunXmlValidator.class.getName());

    /**
     * Validates the document and collects all problems instead of stopping at the first one.
     *
     * @param xmlFile XML document
     * @param xsdFile XSD schema
     * @return list of error messages; empty if the document is valid
     * @throws GunXmlException if a file is missing or the schema itself is invalid
     */
    public List<String> validate(Path xmlFile, Path xsdFile) throws GunXmlException {
        requireFile(xmlFile);
        requireFile(xsdFile);
        Schema schema = loadSchema(xsdFile);
        Validator validator = schema.newValidator();
        CollectingErrorHandler errorHandler = new CollectingErrorHandler();
        validator.setErrorHandler(errorHandler);
        try {
            validator.validate(new StreamSource(xmlFile.toFile()));
        } catch (SAXException e) {
            // a fatal error (malformed XML) was already recorded by the error handler
            LOGGER.fine(() -> "Validation stopped: " + e.getMessage());
        } catch (IOException e) {
            throw new GunXmlException("Cannot read " + xmlFile, e);
        }
        List<String> errors = errorHandler.getErrors();
        if (errors.isEmpty()) {
            LOGGER.info(() -> xmlFile + " is valid against " + xsdFile);
        } else {
            LOGGER.warning(() -> xmlFile + " is NOT valid: " + errors.size() + " error(s)");
        }
        return errors;
    }

    /**
     * @param xmlFile XML document
     * @param xsdFile XSD schema
     * @return {@code true} if the document is valid
     * @throws GunXmlException if a file is missing or the schema itself is invalid
     */
    public boolean isValid(Path xmlFile, Path xsdFile) throws GunXmlException {
        return validate(xmlFile, xsdFile).isEmpty();
    }

    private static Schema loadSchema(Path xsdFile) throws GunXmlException {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return factory.newSchema(xsdFile.toFile());
        } catch (SAXException e) {
            throw new GunXmlException("Invalid XSD schema " + xsdFile + ": " + e.getMessage(), e);
        }
    }

    private static void requireFile(Path file) throws GunXmlException {
        if (file == null || !Files.isRegularFile(file)) {
            throw new GunXmlException("File not found: " + file);
        }
    }

    /** Stores every warning and error with its position in the document. */
    private static final class CollectingErrorHandler implements ErrorHandler {

        private final List<String> errors = new ArrayList<>();

        @Override
        public void warning(SAXParseException e) {
            LOGGER.warning(() -> format(e));
        }

        @Override
        public void error(SAXParseException e) {
            errors.add(format(e));
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            errors.add(format(e));
            throw e;
        }

        List<String> getErrors() {
            return errors;
        }

        private static String format(SAXParseException e) {
            return "line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage();
        }
    }
}
