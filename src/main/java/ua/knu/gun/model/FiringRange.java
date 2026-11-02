package ua.knu.gun.model;

/**
 * Firing range category
 */
public enum FiringRange implements XmlEnum {
    CLOSE("close", 0, 500),
    MEDIUM("medium", 500, 1000),
    FAR("far", 1000, Integer.MAX_VALUE);

    private final String xmlValue;
    private final int minMeters;
    private final int maxMeters;

    FiringRange(String xmlValue, int minMeters, int maxMeters) {
        this.xmlValue = xmlValue;
        this.minMeters = minMeters;
        this.maxMeters = maxMeters;
    }

    @Override
    public String xmlValue() {
        return xmlValue;
    }

    public int getMinMeters() {
        return minMeters;
    }

    public int getMaxMeters() {
        return maxMeters;
    }

    /**
     * @param meters distance in metres
     * @return {@code true} if the distance belongs to this category
     */
    public boolean contains(int meters) {
        return meters >= minMeters && meters < maxMeters;
    }

    /**
     * Determines the category for a distance.
     *
     * @param meters distance in metres, must not be negative
     * @return range category
     */
    public static FiringRange ofDistance(int meters) {
        for (FiringRange range : values()) {
            if (range.contains(meters)) {
                return range;
            }
        }
        throw new IllegalArgumentException("Distance must not be negative: " + meters);
    }
}
