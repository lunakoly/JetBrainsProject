package ru.luna_koly.jetbrainsproject.basic_shapes.util;

/**
 * Created by user on 6/20/17.
 */

public class Scene {
    private float width;
    private float height;
    private float depth;


    public Scene(float width, float height, float depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }
}
