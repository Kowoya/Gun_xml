package ua.knu.gun.exception;

/**
 * Thrown when an XML document cannot be parsed, validated or transformed.
 */
public class GunXmlException extends Exception {

    private static final long serialVersionUID = 1L;

    public GunXmlException(String message) {
        super(message);
    }

    public GunXmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
