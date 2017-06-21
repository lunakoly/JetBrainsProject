package ru.luna_koly.jetbrainsproject.basic_shapes.util;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class Camera {
    private float x, y, z;  // speed
    private Scene scene = null;


    Camera(float x, float y, float z) {
        setPosition(x, y, z);
    }

    public Camera setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Camera setX(float x) {
        this.x = x;
        return this;
    }

    public Camera setY(float y) {
        this.y = y;
        return this;
    }

    public Camera setZ(float z) {
        this.z = z;
        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Camera moveX(float dx) {
        x += dx;

        float f = scene.getWidth() / 2;
        if (x > f) x = f;
        else if (x < -f) x = -f;

        return this;
    }

    public Camera moveY(float dy) {
        y += dy;

        float f = scene.getHeight() / 2;
        if (y > f) y = f;
        else if (y < -f) y = -f;

        return this;
    }

    public Camera moveZ(float dz) {
        z += dz;

        float f = scene.getDepth() / 2;
        if (z > f) z = f;
        else if (z < -f) z = -f;

        return this;
    }

    public Camera move(float dx, float dy, float dz) {
        moveX(dx);
        moveY(dy);
        moveZ(dz);
        return this;
    }

    void restrictToScene(Scene scene) {
        this.scene = scene;
    }
}
