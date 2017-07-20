package com.developer.abdulah.a_star.a_star;

import android.content.Context;
import android.util.Log;

import com.developer.abdulah.a_star.MainActivity;
import com.developer.abdulah.a_star.a_star.graph.BackwardGraph;
import com.developer.abdulah.a_star.a_star.graph.GisGraph;
import com.developer.abdulah.a_star.a_star.graph.Graph;
import com.developer.abdulah.a_star.a_star.node.CoordinatePoint;
import com.developer.abdulah.a_star.a_star.node.Node;

import java.util.Date;

/**
 * Created by dev on 7/19/17.
 */

public class AStarBackgroundWorker {

    public interface SearchResultHandler {
        void onAStarResult(Node[] path);
    }

    private Context context;

    private SearchResultHandler handler;

    public AStarBackgroundWorker(Context context, SearchResultHandler handler) {
        this.context = context.getApplicationContext();
        this.handler = handler;
    }

    public void execute(final CoordinatePoint start, final CoordinatePoint destination) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BackwardGraph graph = new GisGraph(context, start, destination);
                AStar aStar = new BidirectionalGisAStar(graph, start, destination);

                int ts = new Date().getSeconds();

                // consumes cpu time
                Node[] path = aStar.search();

                Log.e("KEK", String.format("A Star finished in %d seconds", (new Date().getSeconds() - ts)));

                handler.onAStarResult(path);
            }
        }).start();
    }
}
