package ua.knu.gun.parser;

/**
 * Creates a parser of the requested type.
 */
public final class GunParserFactory {

    private GunParserFactory() {
    }

    /**
     * @param type parser type
     * @return new parser instance
     */
    public static GunParser create(ParserType type) {
        return switch (type) {
            case SAX -> new SaxGunParser();
            case DOM -> new DomGunParser();
            case STAX -> new StaxGunParser();
        };
    }
}
