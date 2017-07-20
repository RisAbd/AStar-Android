package com.developer.abdulah.a_star.a_star.graph;

import com.developer.abdulah.a_star.a_star.node.Node;

import java.util.List;

/**
 * Created by dev on 7/18/17.
 */

public interface Graph {

    List<Node> neighborsOf(Node node);

    double cost(Node from, Node to);

}
