package ua.knu.gun;

import ua.knu.gun.model.FiringRange;
import ua.knu.gun.model.Gun;
import ua.knu.gun.model.Handy;
import ua.knu.gun.model.Material;
import ua.knu.gun.model.Origin;
import ua.knu.gun.model.Ttc;

import java.nio.file.Path;

/**
 * Paths and expected objects shared by the tests.
 */
public final class TestData {

    public static final Path GUNS_XML = Path.of("data", "guns.xml");
    public static final Path GUN_XSD = Path.of("data", "gun.xsd");
    public static final Path GROUP_BY_ORIGIN_XSL = Path.of("data", "group-by-origin.xsl");

    private static final Path TEST_RESOURCES = Path.of("src", "test", "resources");
    public static final Path INVALID_XML = TEST_RESOURCES.resolve("guns-invalid.xml");
    public static final Path MALFORMED_XML = TEST_RESOURCES.resolve("guns-malformed.xml");
    public static final Path BAD_HANDY_XML = TEST_RESOURCES.resolve("guns-bad-handy.xml");
    public static final Path EXTRA_XML = TEST_RESOURCES.resolve("guns-extra.xml");
    public static final Path BROKEN_XSL = TEST_RESOURCES.resolve("broken.xsl");
    public static final Path BROKEN_XSD = TEST_RESOURCES.resolve("broken.xsd");
    public static final Path MISSING_FILE = TEST_RESOURCES.resolve("no-such-file.xml");

    public static final int GUNS_IN_XML = 13;

    private TestData() {
    }

    /** First firearm of data/guns.xml. */
    public static Gun fort12() {
        return new Gun("g1", Handy.ONE_HANDED, "Fort-12", Origin.UKRAINE,
                new Ttc(FiringRange.CLOSE, 50, true, false), Material.STEEL);
    }

    /** Last firearm of data/guns.xml. */
    public static Gun berettaArx160() {
        return new Gun("g13", Handy.TWO_HANDED, "Beretta ARX160", Origin.ITALY,
                new Ttc(FiringRange.MEDIUM, 600, true, false), Material.POLYMER);
    }

    /** The only firearm of guns-extra.xml. */
    public static Gun fort17() {
        return new Gun("g7", Handy.ONE_HANDED, "Fort-17", Origin.UKRAINE,
                new Ttc(FiringRange.CLOSE, 25, true, false), Material.TITANIUM);
    }

    /** Builds a firearm with only the fields used by comparators. */
    public static Gun gun(String model, Origin origin, FiringRange range, int sightingRange) {
        return new Gun("g1", Handy.TWO_HANDED, model, origin,
                new Ttc(range, sightingRange, true, false), Material.STEEL);
    }
}
