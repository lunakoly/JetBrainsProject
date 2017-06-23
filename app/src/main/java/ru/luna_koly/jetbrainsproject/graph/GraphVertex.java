package ru.luna_koly.jetbrainsproject.graph;

import ru.luna_koly.jetbrainsproject.util.containers.vec2;

/**
 * Created with love by luna_koly on 6/22/17.
 */

public class GraphVertex {
    private Runnable runnable = null;

    public String id = "";
    public vec2 position = new vec2(0, 0);
    public String[] nextVertices = new String[0];

    public void tryReplace(String oldV, String newV) {
        for (int i = 0; i < nextVertices.length; i++)
            if (nextVertices[i].equals(oldV))
                nextVertices[i] = newV;
    }

    @Override
    public String toString() {
        String s = "id = " + id + "; pos = " + position.x + " " + position.y + "; next = ";

        for (int i = 0; i < nextVertices.length; i++)
            s += nextVertices[i] + " ";

        return s;
    }

    public void setAction(Runnable runnable) {
        this.runnable = runnable;
    }

    public void act() {
        runnable.run();
    }

}
