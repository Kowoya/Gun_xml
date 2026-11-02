package ua.knu.gun.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiringRangeTest {

    @Test
    void boundariesFollowTheTask() {
        assertEquals(FiringRange.CLOSE, FiringRange.ofDistance(0));
        assertEquals(FiringRange.CLOSE, FiringRange.ofDistance(499));
        assertEquals(FiringRange.MEDIUM, FiringRange.ofDistance(500));
        assertEquals(FiringRange.MEDIUM, FiringRange.ofDistance(999));
        assertEquals(FiringRange.FAR, FiringRange.ofDistance(1000));
        assertEquals(FiringRange.FAR, FiringRange.ofDistance(25_000));
    }

    @Test
    void negativeDistanceIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> FiringRange.ofDistance(-1));
    }

    @Test
    void containsUsesInclusiveMinAndExclusiveMax() {
        assertTrue(FiringRange.MEDIUM.contains(500));
        assertFalse(FiringRange.MEDIUM.contains(1000));
        assertFalse(FiringRange.MEDIUM.contains(499));
    }

    @Test
    void limitsAreExposed() {
        assertEquals(0, FiringRange.CLOSE.getMinMeters());
        assertEquals(500, FiringRange.CLOSE.getMaxMeters());
        assertEquals(1000, FiringRange.FAR.getMinMeters());
        assertEquals(Integer.MAX_VALUE, FiringRange.FAR.getMaxMeters());
    }
}
