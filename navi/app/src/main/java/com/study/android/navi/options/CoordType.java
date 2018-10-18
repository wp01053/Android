package com.study.android.navi.options;

public enum CoordType {
    /**
     * The World Geodetic System 84
     */
    WGS84("wgs84"),

    /**
     * Katech coordinate system
     */
    @SuppressWarnings("unused")
    KATEC("katec");

    private final String type;

    CoordType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}