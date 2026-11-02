package ua.knu.gun;

import ua.knu.gun.exception.GunXmlException;
import ua.knu.gun.logging.LoggingConfigurator;
import ua.knu.gun.model.Gun;
import ua.knu.gun.parser.GunParserFactory;
import ua.knu.gun.parser.ParserType;
import ua.knu.gun.sort.GunComparators;
import ua.knu.gun.transform.GunXslTransformer;
import ua.knu.gun.validator.GunXmlValidator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point: validates the XML, parses it with SAX, DOM and StAX,
 * sorts the result with comparators and transforms the document with XSL.
 * <p>
 * Arguments (all optional): {@code <xml> <xsd> <xsl> <output>}.
 */
public class GunApplication {

    static final int EXIT_OK = 0;
    static final int EXIT_INVALID_XML = 1;
    static final int EXIT_ERROR = 2;

    private static final Logger LOGGER = Logger.getLogger(GunApplication.class.getName());

    private static final Path DEFAULT_XML = Path.of("data", "guns.xml");
    private static final Path DEFAULT_XSD = Path.of("data", "gun.xsd");
    private static final Path DEFAULT_XSL = Path.of("data", "group-by-origin.xsl");
    private static final Path DEFAULT_OUTPUT = Path.of("target", "guns-by-origin.xml");

    private final GunXmlValidator validator = new GunXmlValidator();
    private final GunXslTransformer transformer = new GunXslTransformer();

    public static void main(String[] args) {
        LoggingConfigurator.configure();
        new GunApplication().run(args);
    }

    /**
     * @param args optional paths: xml, xsd, xsl, output
     * @return exit code: 0 - success, 1 - XML is not valid, 2 - other error
     */
    int run(String[] args) {
        Path xml = argument(args, 0, DEFAULT_XML);
        Path xsd = argument(args, 1, DEFAULT_XSD);
        Path xsl = argument(args, 2, DEFAULT_XSL);
        Path output = argument(args, 3, DEFAULT_OUTPUT);
        try {
            List<String> errors = validator.validate(xml, xsd);
            if (!errors.isEmpty()) {
                errors.forEach(error -> LOGGER.severe(() -> "Validation error: " + error));
                return EXIT_INVALID_XML;
            }
            List<Gun> guns = parseWithAllParsers(xml);
            logSorted(guns);
            transformer.transform(xml, xsl, output);
            return EXIT_OK;
        } catch (GunXmlException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return EXIT_ERROR;
        }
    }

    private static List<Gun> parseWithAllParsers(Path xml) throws GunXmlException {
        List<Gun> guns = List.of();
        for (ParserType type : ParserType.values()) {
            guns = GunParserFactory.create(type).parse(xml);
        }
        return guns;
    }

    private static void logSorted(List<Gun> guns) {
        Map<String, Comparator<Gun>> comparators = new LinkedHashMap<>();
        comparators.put("model", GunComparators.BY_MODEL);
        comparators.put("origin and model", GunComparators.BY_ORIGIN_THEN_MODEL);
        comparators.put("sighting range (desc)", GunComparators.BY_SIGHTING_RANGE_DESC);
        comparators.put("firing range", GunComparators.BY_FIRING_RANGE);

        comparators.forEach((name, comparator) -> {
            List<Gun> sorted = new ArrayList<>(guns);
            sorted.sort(comparator);
            LOGGER.info(() -> "Sorted by " + name + ":");
            sorted.forEach(gun -> LOGGER.info(() -> "  " + gun));
        });
    }

    private static Path argument(String[] args, int index, Path defaultValue) {
        return args.length > index ? Path.of(args[index]) : defaultValue;
    }
}
