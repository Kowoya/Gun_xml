package ua.knu.gun.sort;

import ua.knu.gun.model.Gun;

import java.util.Comparator;

/**
 * Ready-made {@link Comparator}s for sorting firearms.
 */
public final class GunComparators {

    /** Alphabetically by model name, case-insensitive. */
    public static final Comparator<Gun> BY_MODEL =
            Comparator.comparing(Gun::getModel, String.CASE_INSENSITIVE_ORDER);

    /** By country name (case-insensitive, so "Ukraine" goes before "USA"), then by model. */
    public static final Comparator<Gun> BY_ORIGIN_THEN_MODEL =
            Comparator.comparing((Gun gun) -> gun.getOrigin().xmlValue(), String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(BY_MODEL);

    /** Longest sighting range first; equal ranges ordered by model. */
    public static final Comparator<Gun> BY_SIGHTING_RANGE_DESC =
            Comparator.comparingInt((Gun gun) -> gun.getTtc().getSightingRange())
                    .reversed()
                    .thenComparing(BY_MODEL);

    /** By firing range category (close, medium, far), then by sighting range. */
    public static final Comparator<Gun> BY_FIRING_RANGE = new FiringRangeComparator();

    private GunComparators() {
    }
}
