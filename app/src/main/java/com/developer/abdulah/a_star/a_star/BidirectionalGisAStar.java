package com.developer.abdulah.a_star.a_star;

import android.support.annotation.Nullable;

import com.developer.abdulah.a_star.a_star.graph.BackwardGraph;
import com.developer.abdulah.a_star.a_star.graph.Graph;
import com.developer.abdulah.a_star.a_star.heuristics.DistanceHeuristic;
import com.developer.abdulah.a_star.a_star.node.CoordinatePoint;
import com.developer.abdulah.a_star.a_star.node.Node;
import com.developer.abdulah.a_star.a_star.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.developer.abdulah.a_star.util.U.print;
import static java.util.Arrays.asList;
import static java.util.Collections.reverse;

/**
 * Created by dev on 7/19/17.
 */

public class BidirectionalGisAStar implements AStar {

    public static final String TAG = "@GisAStar:";

    private class PathFoundConditionChecker {
        private HashMap<Double, Node> foundLeafs = new HashMap<>();
        private int leafsChecks;
        private boolean aspire;

        private HashMap<Node, Double> costs, bcosts;

        PathFoundConditionChecker(int leafsChecks, boolean aspire, HashMap<Node, Double> costs, HashMap<Node, Double> bcosts) {
            this.leafsChecks = leafsChecks;
            this.costs = costs;
            this.bcosts = bcosts;
            this.aspire = aspire;
        }

        @Nullable
        public Node countAndCheck(Node node) {
            double c = costs.get(node) + bcosts.get(node);
            foundLeafs.put(c, node);
            if (foundLeafs.size() == leafsChecks) {
                Map.Entry<Double, Node> m = null;
                for (Map.Entry<Double, Node> e: foundLeafs.entrySet()) {
                    if (m == null || e.getKey() < m.getKey()) {
                        m = e;
                    }
                }
                return m.getValue();
            }
            return null;
        }
    }

    private BackwardGraph graph;

    private CoordinatePoint start, destination;

    private DistanceHeuristic heuristic;

    private boolean aspire;
    private int leafChecks;

    public BidirectionalGisAStar(
            BackwardGraph graph,
            CoordinatePoint start,
            CoordinatePoint destination,
            DistanceHeuristic heuristic,
            boolean aspire,
            int leafChecks
    ) {
        this.graph = graph;
        this.start = start;
        this.destination = destination;
        this.heuristic = heuristic;
        this.aspire = aspire;
        this.leafChecks = leafChecks;
    }

    public BidirectionalGisAStar(BackwardGraph graph, CoordinatePoint start, CoordinatePoint destination, boolean aspire) {
        this(graph, start, destination, DistanceHeuristic.EUCLIDEAN, aspire, 3);
    }

    public BidirectionalGisAStar(BackwardGraph graph, CoordinatePoint start, CoordinatePoint destination) {
        this(graph, start, destination, false);
    }

    public Node[] search() {

        PriorityQueue<Node> frontier = new PriorityQueue<>();
        PriorityQueue<Node> backtier = new PriorityQueue<>();

        HashMap<Node, Node> prev = new HashMap<>();
        HashMap<Node, Node> bprev = new HashMap<>();

        HashMap<Node, Double> costs = new HashMap<>();
        HashMap<Node, Double> bcosts = new HashMap<>();

        frontier.add(start, 0);
        backtier.add(destination, 0);

        prev.put(start, null);
        bprev.put(destination, null);

        costs.put(start, 0.0);
        bcosts.put(destination, 0.0);

        PathFoundConditionChecker checker = new PathFoundConditionChecker(
                leafChecks, aspire, costs, bcosts);

        Node[] path = new Node[0];

        int i = 0;

        while (!frontier.isEmpty() && !backtier.isEmpty()) {
            i++;

            Node fCurrent = frontier.pop();
            Node bCurrent = backtier.pop();

            if (bprev.containsKey(fCurrent)) {
                Node middle = checker.countAndCheck(fCurrent);
                if (middle != null) {
                    print(TAG, "Finished!");
                    path = recoverBidirectional(start, destination, middle, prev, bprev);
                    break;
                }
            }

            for (Node n: graph.neighborsOf(fCurrent, false)) {

                double newDist = costs.get(fCurrent) + graph.cost(fCurrent, n);

                if (!costs.containsKey(n) || newDist < costs.get(n)) {
                    costs.put(n, newDist);
                    double h = heuristic.distance(n, destination);
                    double priority = newDist + h;

                    frontier.add(n, priority);
                    prev.put(n, fCurrent);
                }
            }

            if (prev.containsKey(bCurrent)) {
                Node middle = checker.countAndCheck(bCurrent);
                if (middle != null) {
                    print(TAG, "Finished!");
                    path = recoverBidirectional(start, destination, middle, prev, bprev);
                    break;
                }
            }

            for (Node n: graph.neighborsOf(bCurrent, true)) {

                double newDist = bcosts.get(bCurrent) + graph.cost(bCurrent, n);

                if (!bcosts.containsKey(n) || newDist < bcosts.get(n)) {
                    bcosts.put(n, newDist);
                    double h = heuristic.distance(n, destination);
                    double priority = newDist + h;

                    backtier.add(n, priority);
                    bprev.put(n, bCurrent);
                }
            }

        }

        print(TAG, String.format("~%d iterations", i*2));

        return path;
    }

    private Node[] recoverBidirectional(Node start, Node destination, Node middle, HashMap<Node, Node> prev, HashMap<Node, Node> bprev) {
        return recoverBidirectional(start, destination, middle, prev, bprev, true);
    }

    private Node[] recoverBidirectional(Node start, Node destination, Node middle, HashMap<Node, Node> prev, HashMap<Node, Node> bprev, boolean reversed) {
        ArrayList<Node> path = new ArrayList<>();
        Node[] sToM = recoverPath(start, middle, prev);
        path.addAll(asList(sToM).subList(0, sToM.length-1));

        ArrayList<Node> dToM = new ArrayList<>(asList(recoverPath(destination, middle, bprev)));
        reverse(dToM);
        path.addAll(dToM);

        if (reversed) {
            reverse(path);
        }

        return path.toArray(new Node[path.size()]);
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
            reverse(path);
        }

        return path.toArray(new Node[path.size()]);
    }

}
