package com.vikmak.distance;

import static java.lang.Math.*;

/**
 * @author Viktor Makarov
 */
public class Calculation {
    public static final double R = 6371.009;

    public static void main(String[] args) {
        double lat1 = 53.2038;
        double lon1 = 50.1606;
        double lat2 = 60.1699;
        double lon2 = 24.9384;

        System.out.println(getDistance2(lat1, lon1, lat2, lon2));
    }

    public static double getDistance2(double lat1, double lon1, double lat2, double lon2) {
        // degrees to radians
        double x1 = lat1 * PI / 180;
        double x2 = lat2 * PI / 180;
        double y1 = lon1 * PI / 180;
        double y2 = lon2 * PI / 180;

        double dy = y1 - y2;

        double dt = acos(sin(x1) * sin(x2) + cos(x1) * cos(x2) * cos(dy));

        double d = R * dt;
        return d;
    }
}
