package ru.luna_koly.jetbrainsproject.graph;

import ru.luna_koly.jetbrainsproject.util.containers.Vector2D;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;

/**
 * Created with love by luna_koly on 22.06.17.
 */

class Line {
    public float k = 0;
    public float b = 0;

    public Line(float k, float b) {
        this.k = k;
        this.b = b;
    }

    public static Line decodePoints(vec2 p1, vec2 p2) {
        float k = (float) Math.tan(Vector2D.getAngleOf2Points(p1, p2));
        float b = p1.y - k * p1.x;
        return new Line(k, b);
    }

    public Line getNormal(vec2 point) {
        float k;
        if (this.k == 0)
            k = Integer.MAX_VALUE;
        else k = -1.0f / this.k;

        float b = point.y - k * point.x;
        return new Line(k, b);
    }

    public float f(float x) {
        return k * x + b;
    }
}
