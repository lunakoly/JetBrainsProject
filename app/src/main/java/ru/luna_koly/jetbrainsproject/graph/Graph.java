package ru.luna_koly.jetbrainsproject.graph;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;

import ru.luna_koly.jetbrainsproject.util.ResourceHolder;
import ru.luna_koly.jetbrainsproject.util.containers.Vector2D;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;

/**
 * Created with love by luna_koly on 6/22/17.
 */

public class Graph extends ResourceHolder<GraphVertex> {
    private ArrayList<Line> lines = new ArrayList<>();
    private String mode = "";

    public Graph() {

    }

    public GraphVertex findNearestGraphPoint(vec2 point) {
        GraphVertex[] vs = new GraphVertex[0];
        vs = values().toArray(vs);
        float min = 0;
        GraphVertex nearest = null;

        for (int i = 0; i < vs.length; i++) {
            float d = Vector2D.getDistanceOf2Points(point, vs[i].position);

            if (i == 0 || d < min) {
                min = d;
                nearest = vs[i];
            }
        }

        return nearest;
    }

    @Override
    public String toString() {
        String[] keys = new String[0];
        keys = keySet().toArray(keys);

        String s = "";

        for (int i = 0; i < keys.length; i++)
            s += get(keys[i]).toString() + "\n";

        return s;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public TrajectoryItem getPath(GraphVertex playerNearest,
                                  GraphVertex destination) {
        HashMap<String, Boolean> used = new HashMap<>();
        String[] s = new String[0];
        s = keySet().toArray(s);

        for (int i = 0; i < s.length; i++)
            used.put(s[i], false);

        return getNextPath(playerNearest, destination, used);
    }

    public TrajectoryItem getNextPath(GraphVertex playerNearest,
                                  GraphVertex destination,
                                  HashMap<String, Boolean> used) {

        if (destination.id.equals(playerNearest.id))
            return new TrajectoryItem(destination.position);

        used.put(playerNearest.id, true);

        for (int i = 0; i < playerNearest.nextVertices.length; i++)
            if (!used.get(playerNearest.nextVertices[i])) {
                TrajectoryItem n = getNextPath(get(playerNearest.nextVertices[i]), destination, used);

                if (n != null) {
                    TrajectoryItem ti = new TrajectoryItem(playerNearest.position);
                    ti.setNext(n);
                    return ti;
                }
            }


        return null;
    }
}
