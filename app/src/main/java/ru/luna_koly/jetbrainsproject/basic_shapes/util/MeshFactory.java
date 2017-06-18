package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import ru.luna_koly.jetbrainsproject.basic_shapes.Triangle;

/**
 * Created with love by iMac on 18.06.17.
 */

public class MeshFactory {

    public static Triangle getExampleTriangle() {
        float[] vertices = {                // counterclockwise
                +0.0f, +0.622008459f, 0.0f,
                -0.5f, -0.311004243f, 0.0f,
                +0.5f, -0.311004243f, 0.0f
        };

        vec3[] vecs = VertexFormatter.splitArrayToVec3(vertices);
        return new Triangle(vecs[0], vecs[1], vecs[2]);
    }

}
