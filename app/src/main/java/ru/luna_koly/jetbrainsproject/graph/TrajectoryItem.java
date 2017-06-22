package ru.luna_koly.jetbrainsproject.graph;

import ru.luna_koly.jetbrainsproject.util.containers.Vector2D;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;

/**
 * Created with love by luna_koly on 22.06.17.
 */

public class TrajectoryItem {
    private static final float NEAR = 0.05f;
    private TrajectoryItem next = null;
    private vec2 position;


    public TrajectoryItem(vec2 position) {
        this.position = position;
    }

    public TrajectoryItem(float x, float y) {
        this(new vec2(x, y));
    }

    public TrajectoryItem next() {
        return next;
    }

    public TrajectoryItem check(vec2 position) {
        float dist = Vector2D.getDistanceOf2Points(position, this.position);

        if (dist <= NEAR)
            return next();


        return this;
    }

    public vec2 getPosition() {
        return position;
    }

    public void setNext(TrajectoryItem next) {
        this.next = next;
    }
}
