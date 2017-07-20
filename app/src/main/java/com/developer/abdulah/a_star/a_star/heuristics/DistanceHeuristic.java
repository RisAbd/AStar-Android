package com.developer.abdulah.a_star.a_star.heuristics;

import com.developer.abdulah.a_star.a_star.node.Point;

/**
 * Created by dev on 7/17/17.
 */

public interface DistanceHeuristic extends Heuristic {

    double distance(Point p1, Point p2);

    DistanceHeuristic MANHATTAN = new DistanceHeuristic() {
        @Override
        public double distance(Point p1, Point p2) {

            double x1 = p1.getX(), y1 = p1.getY();
            double x2 = p2.getX(), y2 = p2.getY();

            return Math.abs(x1-x2) + Math.abs(y1-y2);
        }
    };

    DistanceHeuristic EUCLIDEAN = new DistanceHeuristic() {
        @Override
        public double distance(Point p1, Point p2) {

            double x1 = p1.getX(), y1 = p1.getY();
            double x2 = p2.getX(), y2 = p2.getY();

            return Math.hypot(x1-x2, y1-y2) * 110.03;
        }
    };

}
