package com.developer.abdulah.a_star.a_star.node;

import static com.developer.abdulah.a_star.util.U.print;

/**
 * Created by dev on 7/17/17.
 */

public class Node implements Point {

    public enum Type { POINT, PLATFORM }

    private Object id;
    private Type type;

    private double lat, lng;

    public Node(Object id, Type type, double lat, double lng) {
        this.id = id;

        this.type = type;

        this.lat = lat;
        this.lng = lng;
    }

    public Object getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public double distanceTo(Point another) {
        double aLat = another.getX(), aLng = another.getY();
        return Math.hypot(lat-aLat, lng-aLng) * 111.03;
    }

    @Override
    public double getX() {
        return lat;
    }

    @Override
    public double getY() {
        return lng;
    }

    @Override
    public String toString() {
        return String.format("Node: %s %s %.5f %.5f", id, type, lat, lng);
    }
}
