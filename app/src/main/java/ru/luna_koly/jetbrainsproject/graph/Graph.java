package ru.luna_koly.jetbrainsproject.graph;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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
            float d = findDistance(point, vs[i]);

            if (i == 0 || d < min) {
                min = d;
                nearest = vs[i];
            }
        }

        return nearest;

    }

    private float findDistance(vec2 point, GraphVertex v) {
        float dx = point.x - v.position.x;
        float dy = point.y - v.position.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
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
}
