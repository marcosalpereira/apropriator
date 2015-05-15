package br.com.marcosoft.apropriator.util;

public class Version implements Comparable<Version> {

    private final String version;

    public final String get() {
        return this.version;
    }

    public Version(String version) {
        if (version == null)
            throw new IllegalArgumentException("Version can not be null");
        if (!version.matches("[0-9]+(\\.[0-9]+)*"))
            throw new IllegalArgumentException("Invalid version format");
        this.version = version;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (that == null)
            return false;
        if (this.getClass() != that.getClass())
            return false;
        return this.compareTo((Version) that) == 0;
    }

    public int compareTo(Version that) {
        if (that == null)
            return 1;
        final String[] thisParts = this.get().split("\\.");
        final String[] thatParts = that.get().split("\\.");
        final int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {
            final int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i])
                : 0;
            final int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i])
                : 0;
            if (thisPart < thatPart)
                return -1;
            if (thisPart > thatPart)
                return 1;
        }
        return 0;
    }
    
    @Override
    public String toString() {
    	return version;
    }


}