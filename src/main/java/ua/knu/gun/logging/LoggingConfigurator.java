package ua.knu.gun.logging;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.LogManager;

/**
 * Loads java.util.logging settings from a properties file on the classpath.
 */
public final class LoggingConfigurator {

    /** Default configuration: console + file logs/gun-app.log. */
    public static final String DEFAULT_CONFIG = "/logging.properties";

    /** Directory used by the FileHandler in the default configuration. */
    public static final Path LOG_DIRECTORY = Path.of("logs");

    private LoggingConfigurator() {
    }

    /**
     * Applies the default configuration.
     */
    public static void configure() {
        configure(DEFAULT_CONFIG);
    }

    /**
     * @param resource classpath resource with logging properties
     * @throws IllegalStateException if the resource does not exist
     */
    public static void configure(String resource) {
        try (InputStream config = LoggingConfigurator.class.getResourceAsStream(resource)) {
            if (config == null) {
                throw new IllegalStateException("Logging configuration not found: " + resource);
            }
            Files.createDirectories(LOG_DIRECTORY);
            LogManager.getLogManager().readConfiguration(config);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot configure logging", e);
        }
    }
}
