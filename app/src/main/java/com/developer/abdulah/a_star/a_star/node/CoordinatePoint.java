package com.developer.abdulah.a_star.a_star.node;

import static com.developer.abdulah.a_star.util.U.print;

/**
 * Created by dev on 7/18/17.
 */

public class CoordinatePoint extends Node {

    public enum Id { START, DESTINATION }

    private Id id;

    private double dist;

    public CoordinatePoint(Id id, double dist, double lat, double lng) {
        super(id, Type.POINT, lat, lng);
        this.dist = dist;
    }

    @Override
    public Id getId() {
        return id;
    }

    public double getDist() {
        return dist;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" (%.2f kms)", dist);
    }

    public static void main(String[] args) {
        CoordinatePoint cp = new CoordinatePoint(Id.START, 0.3, 123.321, 321.123);

        print(cp);
    }
}
