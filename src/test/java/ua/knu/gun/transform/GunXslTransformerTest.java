package ua.knu.gun.transform;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.knu.gun.TestData;
import ua.knu.gun.exception.GunXmlException;
import ua.knu.gun.parser.GunXmlTag;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunXslTransformerTest {

    private final GunXslTransformer transformer = new GunXslTransformer();
    private Path tempDir;
    private Path output;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("gun-xsl");
        output = tempDir.resolve("nested").resolve("grouped.xml");
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
    void groupsFirearmsByOrigin() throws Exception {
        transformer.transform(TestData.GUNS_XML, TestData.GROUP_BY_ORIGIN_XSL, output);

        Element root = DocumentBuilderFactory.newDefaultNSInstance()
                .newDocumentBuilder().parse(output.toFile()).getDocumentElement();
        assertEquals("Gun", root.getLocalName());
        assertEquals(GunXmlTag.NAMESPACE, root.getNamespaceURI());

        List<Element> groups = childElements(root);
        Set<String> groupNames = new HashSet<>();
        int firearms = 0;
        for (Element group : groups) {
            assertTrue(groupNames.add(group.getLocalName()), "each origin appears once");
            for (Element firearm : childElements(group)) {
                assertEquals("firearm", firearm.getLocalName());
                String origin = firearm.getElementsByTagNameNS(GunXmlTag.NAMESPACE, "origin")
                        .item(0).getTextContent();
                assertEquals(group.getLocalName(), origin);
                firearms++;
            }
        }
        assertEquals(Set.of("Ukraine", "USA", "Germany", "Belgium", "Austria", "Israel", "Italy"), groupNames);
        assertEquals(TestData.GUNS_IN_XML, firearms);
    }

    @Test
    void attributesAreKeptAndXsiDeclarationIsDropped() throws Exception {
        transformer.transform(TestData.GUNS_XML, TestData.GROUP_BY_ORIGIN_XSL, output);
        String text = Files.readString(output);
        assertTrue(text.contains("id=\"g1\""));
        assertTrue(text.contains("handy=\"one-handed\""));
        assertFalse(text.contains("xmlns:xsi"));
    }

    @Test
    void missingFilesAreReported() {
        assertThrows(GunXmlException.class,
                () -> transformer.transform(TestData.MISSING_FILE, TestData.GROUP_BY_ORIGIN_XSL, output));
        assertThrows(GunXmlException.class,
                () -> transformer.transform(TestData.GUNS_XML, TestData.MISSING_FILE, output));
        assertThrows(GunXmlException.class,
                () -> transformer.transform(null, TestData.GROUP_BY_ORIGIN_XSL, output));
    }

    @Test
    void brokenStylesheetIsReported() {
        assertThrows(GunXmlException.class,
                () -> transformer.transform(TestData.GUNS_XML, TestData.BROKEN_XSL, output));
    }

    @Test
    void malformedSourceIsReportedAndLeavesNoOutput() {
        assertThrows(GunXmlException.class,
                () -> transformer.transform(TestData.MALFORMED_XML, TestData.GROUP_BY_ORIGIN_XSL, output));
        assertTrue(Files.notExists(output), "incomplete result must be removed and its file closed");
    }

    private static List<Element> childElements(Element parent) {
        List<Element> elements = new ArrayList<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) children.item(i));
            }
        }
        return elements;
    }
}
