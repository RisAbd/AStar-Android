package com.developer.abdulah.a_star.a_star.graph;

import com.developer.abdulah.a_star.a_star.node.Node;

import java.util.List;

/**
 * Created by dev on 7/20/17.
 */

public interface BackwardGraph extends Graph {
    List<Node> neighborsOf(Node node, boolean backward);
}
