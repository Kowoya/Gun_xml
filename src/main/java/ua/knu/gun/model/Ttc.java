package ua.knu.gun.model;

import java.util.Objects;

/**
 * Tactical and technical characteristics of a firearm.
 */
public class Ttc {

    private FiringRange firingRange;
    private int sightingRange;
    private boolean clip;
    private boolean optics;

    public Ttc() {
    }

    public Ttc(FiringRange firingRange, int sightingRange, boolean clip, boolean optics) {
        this.firingRange = firingRange;
        this.sightingRange = sightingRange;
        this.clip = clip;
        this.optics = optics;
    }

    public FiringRange getFiringRange() {
        return firingRange;
    }

    public void setFiringRange(FiringRange firingRange) {
        this.firingRange = firingRange;
    }

    /**
     * @return sighting range in metres
     */
    public int getSightingRange() {
        return sightingRange;
    }

    public void setSightingRange(int sightingRange) {
        this.sightingRange = sightingRange;
    }

    public boolean hasClip() {
        return clip;
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public boolean hasOptics() {
        return optics;
    }

    public void setOptics(boolean optics) {
        this.optics = optics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ttc)) {
            return false;
        }
        Ttc other = (Ttc) o;
        return sightingRange == other.sightingRange
                && clip == other.clip
                && optics == other.optics
                && firingRange == other.firingRange;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firingRange, sightingRange, clip, optics);
    }

    @Override
    public String toString() {
        return "Ttc{firingRange=" + firingRange
                + ", sightingRange=" + sightingRange
                + ", clip=" + clip
                + ", optics=" + optics + '}';
    }
}
