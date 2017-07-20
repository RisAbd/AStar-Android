package com.developer.abdulah.a_star;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.developer.abdulah.a_star.a_star.AStarBackgroundWorker;
import com.developer.abdulah.a_star.a_star.GisAStar;
import com.developer.abdulah.a_star.a_star.graph.GisGraph;
import com.developer.abdulah.a_star.a_star.graph.Graph;
import com.developer.abdulah.a_star.a_star.node.CoordinatePoint;
import com.developer.abdulah.a_star.a_star.node.Node;
import com.developer.abdulah.a_star.util.Database;



//import com.developer.abdulah.a_star.util.enc.Database;
//import net.sqlcipher.Cursor;
//import net.sqlcipher.database.SQLiteDatabase;

import java.util.Date;

import static com.developer.abdulah.a_star.a_star.node.CoordinatePoint.Id.DESTINATION;
import static com.developer.abdulah.a_star.a_star.node.CoordinatePoint.Id.START;
import static com.developer.abdulah.a_star.util.U.print;

public class MainActivity extends AppCompatActivity implements AStarBackgroundWorker.SearchResultHandler {

//    AsyncTask<CoordinatePoint, Void, Node[]> task = new AsyncTask<CoordinatePoint, Void, Node[]>() {
//        @Override
//        protected Node[] doInBackground(CoordinatePoint... params) {
//            CoordinatePoint start = params[0];
//            CoordinatePoint destination = params[1];
//
//            Graph graph = new GisGraph(MainActivity.this, start, destination);
//
//            GisAStar aStar = new GisAStar(graph, start, destination);
//
//            int ts = new Date().getSeconds();
//
//            // consumes cpu time
//            Node[] nodes = aStar.search();
//
//            Log.e("KEK", String.format("A Star finished in %d seconds", (new Date().getSeconds() - ts)));
//
//            return nodes;
//        }
//
//        @Override
//        protected void onPostExecute(Node[] nodes) {
//            super.onPostExecute(nodes);
//
//            for (Node n: nodes) {
//                print(n);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.prepDatabase(this);

        CoordinatePoint start = new CoordinatePoint(START, 0.3, 42.795368, 74.513438);
        CoordinatePoint destination = new CoordinatePoint(DESTINATION, 0.3, 42.912733428491066, 74.51852560043336);

        AStarBackgroundWorker kek = new AStarBackgroundWorker(this, this);
        kek.execute(start, destination);

    }

    @Override
    public void onAStarResult(Node[] path) {
        for (Node n: path) {
            print(n);
        }
    }
}
