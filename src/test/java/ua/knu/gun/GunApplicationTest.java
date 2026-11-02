package ua.knu.gun;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunApplicationTest {

    private final GunApplication application = new GunApplication();
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("gun-app");
    }

    @AfterEach
    void tearDown() throws IOException {
        try (Stream<Path> files = Files.walk(tempDir)) {
            for (Path file : files.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(file);
            }
        }
    }

    @Test
    void runsWholePipelineWithDefaultFiles() {
        assertEquals(GunApplication.EXIT_OK, application.run(new String[0]));
        assertTrue(Files.exists(Path.of("target", "guns-by-origin.xml")));
    }

    @Test
    void writesResultToGivenFile() {
        Path output = tempDir.resolve("out.xml");
        assertEquals(GunApplication.EXIT_OK, application.run(args(TestData.GUNS_XML, output)));
        assertTrue(Files.exists(output));
    }

    @Test
    void stopsOnInvalidDocument() {
        Path output = tempDir.resolve("out.xml");
        assertEquals(GunApplication.EXIT_INVALID_XML, application.run(args(TestData.INVALID_XML, output)));
        assertTrue(Files.notExists(output));
    }

    @Test
    void reportsMissingFile() {
        assertEquals(GunApplication.EXIT_ERROR,
                application.run(args(TestData.MISSING_FILE, tempDir.resolve("out.xml"))));
    }

    @Test
    void mainConfiguresLoggingAndRuns() {
        Path output = tempDir.resolve("main.xml");
        assertDoesNotThrow(() -> GunApplication.main(args(TestData.GUNS_XML, output)));
        assertTrue(Files.exists(output));
    }

    private static String[] args(Path xml, Path output) {
        return new String[] {xml.toString(), TestData.GUN_XSD.toString(),
                TestData.GROUP_BY_ORIGIN_XSL.toString(), output.toString()};
    }
}
