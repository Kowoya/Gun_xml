package ua.knu.gun.model;

import org.junit.jupiter.api.Test;
import ua.knu.gun.TestData;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GunTest {

    @Test
    void settersAndGettersWork() {
        Ttc ttc = new Ttc(FiringRange.FAR, 1200, true, true);
        Gun gun = new Gun();
        gun.setId("g42");
        gun.setHandy(Handy.TWO_HANDED);
        gun.setModel("Z-008");
        gun.setOrigin(Origin.UKRAINE);
        gun.setTtc(ttc);
        gun.setMaterial(Material.STEEL);

        assertEquals("g42", gun.getId());
        assertEquals(Handy.TWO_HANDED, gun.getHandy());
        assertEquals("Z-008", gun.getModel());
        assertEquals(Origin.UKRAINE, gun.getOrigin());
        assertEquals(ttc, gun.getTtc());
        assertEquals(Material.STEEL, gun.getMaterial());
    }

    @Test
    void equalObjectsHaveEqualHashCodes() {
        Gun gun = TestData.fort12();
        assertEquals(gun, gun);
        assertEquals(TestData.fort12(), gun);
        assertEquals(TestData.fort12().hashCode(), gun.hashCode());
    }

    @Test
    void everyFieldTakesPartInEquals() {
        assertDiffers(gun -> gun.setId("g99"));
        assertDiffers(gun -> gun.setHandy(Handy.TWO_HANDED));
        assertDiffers(gun -> gun.setModel("Fort-14"));
        assertDiffers(gun -> gun.setOrigin(Origin.USA));
        assertDiffers(gun -> gun.setTtc(new Ttc(FiringRange.CLOSE, 25, true, false)));
        assertDiffers(gun -> gun.setMaterial(Material.POLYMER));
    }

    @Test
    void isNotEqualToNullOrOtherType() {
        Gun gun = TestData.fort12();
        assertFalse(gun.equals(null));
        assertFalse(gun.equals("Fort-12"));
    }

    @Test
    void toStringContainsMainFields() {
        String text = TestData.fort12().toString();
        assertTrue(text.contains("g1"));
        assertTrue(text.contains("Fort-12"));
        assertTrue(text.contains("UKRAINE"));
    }

    private static void assertDiffers(Consumer<Gun> change) {
        Gun changed = TestData.fort12();
        change.accept(changed);
        assertNotEquals(TestData.fort12(), changed);
    }
}
