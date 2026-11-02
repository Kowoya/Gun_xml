package ua.knu.gun.validator;

import org.junit.jupiter.api.Test;
import ua.knu.gun.TestData;
import ua.knu.gun.exception.GunXmlException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunXmlValidatorTest {

    private final GunXmlValidator validator = new GunXmlValidator();

    @Test
    void mainDocumentIsValid() throws GunXmlException {
        assertTrue(validator.validate(TestData.GUNS_XML, TestData.GUN_XSD).isEmpty());
        assertTrue(validator.isValid(TestData.GUNS_XML, TestData.GUN_XSD));
    }

    @Test
    void everyBrokenRuleIsReported() throws GunXmlException {
        List<String> errors = validator.validate(TestData.INVALID_XML, TestData.GUN_XSD);
        String all = String.join("\n", errors);

        assertTrue(all.contains("5000"), "upper bound of sighting-range");
        assertTrue(all.contains("g1"), "duplicate ID");
        assertTrue(all.contains("Atlantis"), "enumeration of origin");
        assertTrue(all.contains("handy"), "required attribute");
        assertTrue(all.contains("yes"), "xs:boolean");
        assertTrue(errors.stream().allMatch(error -> error.startsWith("line ")), "position is reported");
        assertFalse(validator.isValid(TestData.INVALID_XML, TestData.GUN_XSD));
    }

    @Test
    void malformedDocumentIsNotValid() throws GunXmlException {
        List<String> errors = validator.validate(TestData.MALFORMED_XML, TestData.GUN_XSD);
        assertFalse(errors.isEmpty());
    }

    @Test
    void missingFilesAreReported() {
        assertThrows(GunXmlException.class, () -> validator.validate(TestData.MISSING_FILE, TestData.GUN_XSD));
        assertThrows(GunXmlException.class, () -> validator.validate(TestData.GUNS_XML, TestData.MISSING_FILE));
        assertThrows(GunXmlException.class, () -> validator.validate(null, TestData.GUN_XSD));
    }

    @Test
    void brokenSchemaIsReported() {
        GunXmlException e = assertThrows(GunXmlException.class,
                () -> validator.validate(TestData.GUNS_XML, TestData.BROKEN_XSD));
        assertTrue(e.getMessage().contains("Invalid XSD"));
    }
}
