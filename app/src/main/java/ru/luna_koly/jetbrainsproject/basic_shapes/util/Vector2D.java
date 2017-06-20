package ru.luna_koly.jetbrainsproject.basic_shapes.util;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class Vector2D {
    private float module = 0;
    private double angle = 0;
    private vec2 p       = new vec2(0, 0);


    public Vector2D(vec2 p1, vec2 p2) {
        p = p1;

        module = getDistanceOf2Points(p1, p2);
        angle  = getAngleOf2Points(p1, p2);
    }

    public Vector2D(float module, double angle, vec2 start) {
        this.module = module;
        this.angle = angle;
        this.p = start;
    }

    public float getModule() {
        return module;
    }

    public double getAngle() {
        return angle;
    }

    public vec2 getStart() {
        return p;
    }

    public void setModule(float module) {
        this.module = module;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setP(vec2 p) {
        this.p = p;
    }


    public static double getAngleOf2Points(vec2 p1, vec2 p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        double angle     = 0;
        double corrector = 0;

        if (dx == 0) { if (dy != 0) corrector = Math.PI / 2; }
        else angle = Math.atan(dy / dx);

        if (dx < 0) corrector = Math.PI;
        if (dy < 0) corrector = Math.PI * 2 - corrector;

        return angle + corrector;
    }

    public static float getDistanceOf2Points(vec2 p1, vec2 p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
