package com.developer.abdulah.a_star.a_star;

import com.developer.abdulah.a_star.a_star.graph.Graph;
import com.developer.abdulah.a_star.a_star.heuristics.DistanceHeuristic;
import com.developer.abdulah.a_star.a_star.heuristics.Heuristic;
import com.developer.abdulah.a_star.a_star.node.CoordinatePoint;
import com.developer.abdulah.a_star.a_star.node.Node;
import com.developer.abdulah.a_star.a_star.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.developer.abdulah.a_star.util.U.print;

/**
 * Created by dev on 7/18/17.
 */

public class GisAStar implements AStar {

    public static final String TAG = "@GisAStar:";

    private Graph graph;

    private CoordinatePoint start, destination;

    private DistanceHeuristic heuristic;

    public GisAStar(Graph graph, CoordinatePoint start, CoordinatePoint destination, DistanceHeuristic heuristic) {
        this.graph = graph;
        this.start = start;
        this.destination = destination;
        this.heuristic = heuristic;
    }

    public GisAStar(Graph graph, CoordinatePoint start, CoordinatePoint destination) {
        this(graph, start, destination, DistanceHeuristic.EUCLIDEAN);
    }

    public Node[] search() {

        PriorityQueue<Node> frontier = new PriorityQueue<>();
        HashMap<Node, Node> prev = new HashMap<>();
        HashMap<Node, Double> costs = new HashMap<>();

        frontier.add(start, 0);
        prev.put(start, null);
        costs.put(start, 0.0);

        Node[] path = new Node[0];

        int i = 0;

        while (!frontier.isEmpty()) {
            i++;

            Node current = frontier.pop();

            if (current == destination) {
                print(TAG, "Finished!");
                path = recoverPath(start, destination, prev);
                break;
            }

            for (Node n: graph.neighborsOf(current)) {

                double newDist = costs.get(current) + graph.cost(current, n);

                if (!costs.containsKey(n) || newDist < costs.get(n)) {
                    costs.put(n, newDist);
                    double h = heuristic.distance(n, destination);
                    double priority = newDist + h;

                    frontier.add(n, priority);
                    prev.put(n, current);
                }
            }

        }

        print(TAG, String.format("%d iterations", i));

        return path;
    }

    private Node[] recoverPath(Node start, Node destination, HashMap<Node, Node> prev) {
        return recoverPath(start, destination, prev, false);
    }

    private Node[] recoverPath(Node start, Node destination, HashMap<Node, Node> prev, boolean reverse) {
        ArrayList<Node> path = new ArrayList<>();
        Node p = destination;
        while (p != start) {
            path.add(p);
            p = prev.get(p);
        }
        path.add(p);

        if (!reverse) {
            Collections.reverse(path);
        }

        return path.toArray(new Node[path.size()]);
    }


}
