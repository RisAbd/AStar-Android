package com.developer.abdulah.a_star.util;

import android.text.TextUtils;

import com.developer.abdulah.a_star.a_star.node.Point;

/**
 * Created by dev on 7/17/17.
 */

public class U {

    public static void print(Object... os) {
        StringBuilder sb = new StringBuilder();
        for (Object o: os) {
            sb.append(o);
            sb.append(' ');
        }
        System.out.println(sb.toString());
    }

    public static double distance(Point p1, Point p2) {
        double x1 = p1.getX(), y1 = p1.getY();
        double x2 = p2.getX(), y2 = p2.getY();

        return Math.hypot(x1-x2, y1-y2);
    }
}
