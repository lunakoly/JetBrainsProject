package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.util.Log;

import java.util.Arrays;

/**
 * Created with love by iMac on 18.06.17.
 */

public class VertexFormatter {
    public static float[] getVertices(vec3... points) {
        float[] vertices = new float[points.length * 3];

        for (int i = 0; i < points.length; i++) {
            vertices[i * 3] = points[i].x;
            vertices[i * 3 + 1] = points[i].y;
            vertices[i * 3 + 2] = points[i].z;
        }

        return vertices;
    }

    public static vec3[] splitArrayToVec3(float... array) {
        int length = array.length / 3;
        vec3[] vecs = new vec3[length];

        for (int i = 0; i < length; i++)
            vecs[i] = new vec3(array[i * 3],
                               array[i * 3 + 1],
                               array[i * 3 + 2]);

        return vecs;
    }

    public static float[] generateEmptyVertexArray(int amount) {
        float[] vertices = new float[amount];

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = 0.0f;
        }

        return vertices;
    }

    public static float[] translateToPosition(vec3 pos, float... vertices) {
        float[] relative = new float[vertices.length];

        for (int i = 0; i < vertices.length / 3; i++) {
            relative[i * 3]     = vertices[i * 3]     + pos.x;
            relative[i * 3 + 1] = vertices[i * 3 + 1] + pos.y;
            relative[i * 3 + 2] = vertices[i * 3 + 2] + pos.z;
        }

        return relative;
    }

    public static float[] getTriad(int index, float... vertices) {
        return Arrays.copyOfRange(vertices, index * 3, index * 3 + 3);
    }

    public static float[] getTriadOfPattern(float[] vertices, short[] pattern) {
        float[] all = new float[pattern.length * 3];

        for (int i = 0; i < pattern.length; i++) {
            System.arraycopy(getTriad( pattern[i], vertices ), 0, all, i * 3, 3);
        }

        return all;
    }

    public static float[] zoom2D(vec3 position, float power, float... vertices) {
        for (int i = 0; i < vertices.length / 3; i++) {
            float dx = vertices[i * 3]     - position.x;
            float dy = vertices[i * 3 + 1] - position.y;
            float dz = vertices[i * 3 + 2] - position.z;

            vertices[i * 3]     += dx * power;
            vertices[i * 3 + 1] += dy * power;
            vertices[i * 3 + 2] += dz * power;
        }

        return vertices;
    }
}
