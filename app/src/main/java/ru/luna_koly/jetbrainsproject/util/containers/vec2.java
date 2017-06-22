package ru.luna_koly.jetbrainsproject.util.containers;

/**
 * Created with love by iMac on 18.06.17.
 */

public class vec2 {
    public float x, y, r, g;

    public vec2(float x, float y) {
        this.r = this.x = x;
        this.g = this.y = y;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }
}
