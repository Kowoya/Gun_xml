package ua.knu.gun.sort;

import ua.knu.gun.model.Gun;

import java.util.Comparator;

/**
 * Orders firearms by firing range category (close, medium, far)
 * and, inside one category, by sighting range in ascending order.
 */
public class FiringRangeComparator implements Comparator<Gun> {

    @Override
    public int compare(Gun first, Gun second) {
        int byCategory = first.getTtc().getFiringRange().compareTo(second.getTtc().getFiringRange());
        if (byCategory != 0) {
            return byCategory;
        }
        return Integer.compare(first.getTtc().getSightingRange(), second.getTtc().getSightingRange());
    }
}
