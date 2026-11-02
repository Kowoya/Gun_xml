package ua.knu.gun.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TtcTest {

    private static Ttc sample() {
        return new Ttc(FiringRange.MEDIUM, 600, true, true);
    }

    @Test
    void settersAndGettersWork() {
        Ttc ttc = new Ttc();
        ttc.setFiringRange(FiringRange.FAR);
        ttc.setSightingRange(1500);
        ttc.setClip(true);
        ttc.setOptics(true);

        assertEquals(FiringRange.FAR, ttc.getFiringRange());
        assertEquals(1500, ttc.getSightingRange());
        assertTrue(ttc.hasClip());
        assertTrue(ttc.hasOptics());
    }

    @Test
    void equalObjectsHaveEqualHashCodes() {
        Ttc ttc = sample();
        assertEquals(ttc, ttc);
        assertEquals(sample(), ttc);
        assertEquals(sample().hashCode(), ttc.hashCode());
    }

    @Test
    void everyFieldTakesPartInEquals() {
        Ttc ttc = sample();
        assertNotEquals(new Ttc(FiringRange.FAR, 600, true, true), ttc);
        assertNotEquals(new Ttc(FiringRange.MEDIUM, 700, true, true), ttc);
        assertNotEquals(new Ttc(FiringRange.MEDIUM, 600, false, true), ttc);
        assertNotEquals(new Ttc(FiringRange.MEDIUM, 600, true, false), ttc);
        assertFalse(ttc.equals(null));
        assertFalse(ttc.equals("ttc"));
    }

    @Test
    void toStringContainsValues() {
        String text = sample().toString();
        assertTrue(text.contains("MEDIUM"));
        assertTrue(text.contains("600"));
    }
}
