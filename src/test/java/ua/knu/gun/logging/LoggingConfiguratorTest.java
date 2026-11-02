package ua.knu.gun.logging;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingConfiguratorTest {

    @Test
    void defaultConfigurationIsApplied() {
        LoggingConfigurator.configure();

        assertTrue(Files.isDirectory(LoggingConfigurator.LOG_DIRECTORY));
        assertEquals(Level.FINE, Logger.getLogger("ua.knu.gun").getLevel());
    }

    @Test
    void missingConfigurationIsReported() {
        assertThrows(IllegalStateException.class, () -> LoggingConfigurator.configure("/no-such.properties"));
    }
}
