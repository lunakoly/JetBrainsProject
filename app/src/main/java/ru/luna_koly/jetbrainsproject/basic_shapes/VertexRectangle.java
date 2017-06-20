package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by iMac on 18.06.17.
 */

public class VertexRectangle extends AbstractVertexShape {
    private static final short[] drawOrder = {0, 1, 2, 2, 3, 0};


    public VertexRectangle(float[] vertices) {
        if (vertices.length != 12)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        this.vertices = VertexFormatter.getTriadOfPattern(vertices, drawOrder);
        resetRelativeVertices();
        genBuffer();

        // set to default
        shaderProgram = GameRenderer.getDefaultShaderProgram();
    }

    public VertexRectangle(vec3 p1, vec3 p2, vec3 p3, vec3 p4) {
        this(VertexFormatter.getVertices(p1, p2, p3, p4));
    }

    public void setVertexAttributePointer(int vertexPositionAttribute) {
        GLES20.glVertexAttribPointer(vertexPositionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(shaderProgram);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(shaderProgram, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        externalDraw(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
    }
}
