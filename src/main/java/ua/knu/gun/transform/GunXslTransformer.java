package ua.knu.gun.transform;

import ua.knu.gun.exception.GunXmlException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Applies an XSL stylesheet to an XML document.
 */
public class GunXslTransformer {

    private static final Logger LOGGER = Logger.getLogger(GunXslTransformer.class.getName());

    /**
     * @param xmlFile    source document
     * @param xslFile    XSL stylesheet
     * @param outputFile result file (parent directories are created if needed)
     * @throws GunXmlException if a file is missing or the transformation fails
     */
    public void transform(Path xmlFile, Path xslFile, Path outputFile) throws GunXmlException {
        requireFile(xmlFile);
        requireFile(xslFile);
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setErrorListener(new LoggingErrorListener());
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile.toFile()));
            Files.createDirectories(outputFile.toAbsolutePath().getParent());
            writeResult(transformer, xmlFile, outputFile);
        } catch (TransformerException | IOException e) {
            throw new GunXmlException("Cannot transform " + xmlFile + " with " + xslFile
                    + ": " + e.getMessage(), e);
        }
        LOGGER.info(() -> "Transformed " + xmlFile + " -> " + outputFile);
    }

    /**
     * Opens the streams ourselves so they are always closed, even when the transformation fails.
     * A processor given a File may leave it open after an error, and Windows then cannot
     * delete or overwrite that file. An incomplete result is removed.
     */
    private static void writeResult(Transformer transformer, Path xmlFile, Path outputFile)
            throws TransformerException, IOException {
        boolean completed = false;
        try (InputStream input = Files.newInputStream(xmlFile);
             OutputStream output = Files.newOutputStream(outputFile)) {
            StreamSource source = new StreamSource(input, xmlFile.toUri().toString());
            transformer.transform(source, new StreamResult(output));
            completed = true;
        } finally {
            if (!completed) {
                Files.deleteIfExists(outputFile);
            }
        }
    }

    private static void requireFile(Path file) throws GunXmlException {
        if (file == null || !Files.isRegularFile(file)) {
            throw new GunXmlException("File not found: " + file);
        }
    }

    /** Sends stylesheet problems to the logger instead of System.err. */
    private static final class LoggingErrorListener implements ErrorListener {

        @Override
        public void warning(TransformerException e) {
            LOGGER.warning(e::getMessageAndLocation);
        }

        @Override
        public void error(TransformerException e) {
            LOGGER.severe(e::getMessageAndLocation);
        }

        @Override
        public void fatalError(TransformerException e) throws TransformerException {
            LOGGER.severe(e::getMessageAndLocation);
            throw e;
        }
    }
}
