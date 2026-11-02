package ua.knu.gun.sort;

import org.junit.jupiter.api.Test;
import ua.knu.gun.model.FiringRange;
import ua.knu.gun.model.Gun;
import ua.knu.gun.model.Origin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.knu.gun.TestData.gun;

class GunComparatorsTest {

    private static final Gun Z008 = gun("Z-008", Origin.UKRAINE, FiringRange.FAR, 1200);
    private static final Gun BARRETT = gun("Barrett M82A1", Origin.USA, FiringRange.FAR, 1800);
    private static final Gun GLOCK = gun("Glock 17", Origin.AUSTRIA, FiringRange.CLOSE, 50);
    private static final Gun FORT = gun("fort-12", Origin.UKRAINE, FiringRange.CLOSE, 50);
    private static final Gun G36 = gun("HK G36", Origin.GERMANY, FiringRange.MEDIUM, 800);
    private static final Gun MOSSBERG = gun("Mossberg 500", Origin.USA, FiringRange.CLOSE, 40);

    private static final List<Gun> GUNS = List.of(Z008, BARRETT, GLOCK, FORT, G36, MOSSBERG);

    @Test
    void sortsByModelIgnoringCase() {
        assertEquals(List.of(BARRETT, FORT, GLOCK, G36, MOSSBERG, Z008), sorted(GunComparators.BY_MODEL));
    }

    @Test
    void sortsByOriginThenModel() {
        assertEquals(List.of(GLOCK, G36, FORT, Z008, BARRETT, MOSSBERG),
                sorted(GunComparators.BY_ORIGIN_THEN_MODEL));
    }

    @Test
    void sortsBySightingRangeDescendingThenModel() {
        assertEquals(List.of(BARRETT, Z008, G36, FORT, GLOCK, MOSSBERG),
                sorted(GunComparators.BY_SIGHTING_RANGE_DESC));
    }

    @Test
    void sortsByFiringRangeCategoryThenSightingRange() {
        List<Gun> result = sorted(GunComparators.BY_FIRING_RANGE);
        assertEquals(MOSSBERG, result.get(0));
        assertEquals(G36, result.get(3));
        assertEquals(List.of(Z008, BARRETT), result.subList(4, 6));
    }

    @Test
    void firingRangeComparatorTreatsEqualCharacteristicsAsEqual() {
        assertEquals(0, new FiringRangeComparator().compare(GLOCK, FORT));
    }

    private static List<Gun> sorted(Comparator<Gun> comparator) {
        List<Gun> copy = new ArrayList<>(GUNS);
        copy.sort(comparator);
        return copy;
    }
}
