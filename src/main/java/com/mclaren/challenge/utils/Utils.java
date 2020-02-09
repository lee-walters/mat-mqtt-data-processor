package com.mclaren.challenge.utils;

import static org.apache.lucene.util.SloppyMath.haversinMeters;

public class Utils {

    /**
     * Support by apache lucene library - function to calculate distance between to GPS points
     *
     * @param lat1
     * @param long1
     * @param lat2
     * @param long2
     * @return
     */
    public static double calculateDistanceInMeters(double lat1, double long1, double lat2, double long2) {
        return haversinMeters(lat1, long1, lat2, long2);
    }
}
