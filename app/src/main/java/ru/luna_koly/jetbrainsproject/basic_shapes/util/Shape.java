package ru.luna_koly.jetbrainsproject.basic_shapes.util;

/**
 * Created with love by iMac on 18.06.17.
 */

public interface Shape {
    void draw();
    void externalDraw(int vertexPositionAttribute);
    void recalculateVertices(Camera camera);
    void rescale(vec3 center, float power);
    void resetRelativeVertices();
}
