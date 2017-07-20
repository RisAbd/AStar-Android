package com.developer.abdulah.a_star.a_star.graph;

import android.content.Context;

import com.developer.abdulah.a_star.a_star.node.CoordinatePoint;
import com.developer.abdulah.a_star.a_star.node.Node;
import com.developer.abdulah.a_star.a_star.node.Platform;
import com.developer.abdulah.a_star.util.Database;

import java.util.ArrayList;
import java.util.List;

import static com.developer.abdulah.a_star.util.U.print;

/**
 * Created by dev on 7/18/17.
 */

public class GisGraph implements BackwardGraph {

    public interface Costs {
        double WALK = 10;
        double TRANSFER = 40;
        double TRANSFER_SAME = -3;
        double SHIFT = 2;

        Costs DEFAULT = new Costs() {};
    }

    private CoordinatePoint start, destination;

    private double platformWalkDist;
    private Costs costs;

    private List<Node> startPlatforms;
    private List<Node> destinationPlatforms;

    private List<Platform> platforms;


    public GisGraph(Context context, CoordinatePoint start, CoordinatePoint destination, double platformWalkDist, Costs costs) {
        this.start = start;
        this.destination = destination;
        this.platformWalkDist = platformWalkDist;
        this.costs = costs;

        platforms = Database.getInstance(context).allPlatforms();
        startPlatforms = pointNeighbors(start);
        destinationPlatforms = pointNeighbors(destination);
    }

    public GisGraph(Context context, CoordinatePoint start, CoordinatePoint destination) {
        this(context, start, destination, 0.3, Costs.DEFAULT);
    }

    private List<Node> pointNeighbors(CoordinatePoint point) {
        return pointNeighbors(point, point.getDist());
    }

    private List<Node> pointNeighbors(Node point, double dist) {
        ArrayList<Node> nearests = new ArrayList<>();

        for (Platform p: platforms) {
            if (point.distanceTo(p) < dist) {
                nearests.add(p);
            }
        }

        // TODO:
        if (nearests.isEmpty()) {
            throw new RuntimeException("TODO: Implement nearests search if no platforms found in distance");
        }

        return nearests;
    }


    private List<Node> platformNeighbors(Platform platform, boolean backward) {

        int pid = platform.getId();
        int sid = platform.getStationId();
        int did = platform.getDirId();
        int dpos = platform.getDirPos();
        int nd = backward ? -1 : 1;

        ArrayList<Node> sameStationTransfers = new ArrayList<>();
        ArrayList<Node> samePlatformTransfers = new ArrayList<>();
        ArrayList<Node> nextPlatforms = new ArrayList<>();
        ArrayList<Node> walkTransfers = new ArrayList<>();
        ArrayList<Node> destinationNodes = new ArrayList<>();

        if (backward && startPlatforms.contains(platform)) {
            destinationNodes.add(start);
        } else if (destinationPlatforms.contains(platform)) {
            destinationNodes.add(destination);
        }

        for (Platform p: platforms) {

            if (p == platform) {
                continue;
            }

            int npid = p.getId();
            int nsid = p.getStationId();
            int ndid = p.getDirId();
            int ndpos = p.getDirPos();

            if (ndid == did && ndpos == dpos + nd) {
                nextPlatforms.add(p);
            } else if (npid == pid && ndid != did) {
                samePlatformTransfers.add(p);
            } else if (nsid == sid && npid != pid) {
                sameStationTransfers.add(p);
            } else if (platform.distanceTo(p) < platformWalkDist) {
                walkTransfers.add(p);
            }
        }

        destinationNodes.addAll(nextPlatforms);
        destinationNodes.addAll(samePlatformTransfers);
        destinationNodes.addAll(walkTransfers);
        destinationNodes.addAll(sameStationTransfers);

        return destinationNodes;
    }

    @Override
    public double cost(Node from, Node to) {

        if (from == to || from == null || to == null) {
            return 0;
        }

        if (from instanceof CoordinatePoint) {
            CoordinatePoint f = (CoordinatePoint) from;
            return costs.WALK * f.distanceTo(to);
        } else if (to instanceof CoordinatePoint) {
            CoordinatePoint t = (CoordinatePoint) to;
            return costs.WALK * t.distanceTo(from);
        }

        Platform f = (Platform) from;
        Platform t = (Platform) to;

        int fid = f.getId();
        int fsid = f.getStationId();
        int fdid = f.getDirId();

        int tid = t.getId();
        int tsid = t.getStationId();
        int tdid = t.getDirId();

        if (fdid == tdid) {

            // TODO: measure !direction! line length on shift
            
            return costs.SHIFT;
        }

        double cost = costs.TRANSFER;
        if (fid == tid) {
            cost += costs.TRANSFER_SAME;
        }
        if (fsid != tsid) {
            cost += costs.WALK * f.distanceTo(t);
        }

        return cost;
    }


    @Override
    public List<Node> neighborsOf(Node node) {
        return neighborsOf(node, false);
    }

    @Override
    public List<Node> neighborsOf(Node node, boolean backward) {

        if (node instanceof CoordinatePoint) {
            return pointNeighbors((CoordinatePoint) node);
        } else if (node instanceof Platform) {
            return platformNeighbors((Platform) node, backward);
        }

        throw new RuntimeException("WTF, node misunderstood");
    }

}
