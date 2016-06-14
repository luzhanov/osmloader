package com.waypointer.osmloader.utils;

/**
* @author Igor Luzhanov, 10.01.2015
*/
public class Coordinate {

    private final double latitude;
    private final double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
