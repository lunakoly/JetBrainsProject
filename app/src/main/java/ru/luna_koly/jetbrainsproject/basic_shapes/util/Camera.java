package ru.luna_koly.jetbrainsproject.basic_shapes.util;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class Camera {
    private float x, y, z;  // speed
    private float zoom = 1;


    public Camera(float x, float y, float z) {
        setPosition(x, y, z);
    }

    public Camera(vec3 position) {
        setPosition(position);
    }

    public Camera setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Camera setPosition(vec3 position) {
        return setPosition(position.x, position.y, position.z);
    }

    public vec3 getPosition() {
        return new vec3(x, y, z);
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
        return this;
    }

    public Camera moveY(float dy) {
        y += dy;
        return this;
    }

    public Camera moveZ(float dz) {
        z += dz;
        return this;
    }

    public Camera move(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
        return this;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
