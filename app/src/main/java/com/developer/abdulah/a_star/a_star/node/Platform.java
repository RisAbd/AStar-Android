package com.developer.abdulah.a_star.a_star.node;

import static com.developer.abdulah.a_star.util.U.print;

/**
 * Created by dev on 7/18/17.
 */

public class Platform extends Node {

    private int sid, did, dpos;

    public Platform(int id, int sid, int did, int dpos, double lat, double lng) {
        super(id, Type.PLATFORM, lat, lng);
        this.sid = sid;
        this.did = did;
        this.dpos = dpos;
    }

    @Override
    public Integer getId() {
        return (int) super.getId();
    }

    public int getStationId() {
        return sid;
    }

    public int getDirId() {
        return did;
    }

    public int getDirPos() {
        return dpos;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" (%d %d %d)", sid, did, dpos);
    }

    public static void main(String[] args) {
        Platform p = new Platform(123, 124, 12, 1, 123.234, 123.321);

        print(p);
    }
}
