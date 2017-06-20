package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.util.Log;

import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by luna_koly on 18.06.17.
 */

public class VertexTriangle extends AbstractVertexShape {
    public VertexTriangle(float[] vertices) {
        if (vertices.length != 9)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        genBuffer();

        // set to default
        shaderProgram = GameRenderer.getDefaultShaderProgram().getCurrentProgram();
    }

    public VertexTriangle(vec3 p1, vec3 p2, vec3 p3) {
        this(VertexFormatter.getVertices(p1, p2, p3));
    }
}
