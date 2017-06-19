package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.util.Log;

import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created by user on 6/19/17.
 */

public class VertexQuadOfTriangles implements Shape {
    private VertexTriangle[] triangles = new VertexTriangle[2];

    public VertexQuadOfTriangles(float[] vertices) {
        if (vertices.length != 12)
            vertices = VertexFormatter.generateEmptyVertexArray(9);

        Log.d("quad", "" + Arrays.toString(vertices));

        // get triangle 1
        triangles[0] = new VertexTriangle(Arrays.copyOfRange(vertices, 0, 9));

        // get triangle 2
        float[] secvert = new float[9];

        // take p1
        float[] p1 = {
                vertices[0], vertices[1], vertices[2]
        };

        // take p3, p4
        System.arraycopy(p1, 0, secvert, 0, 3);
        System.arraycopy(
                Arrays.copyOfRange(vertices, 6, 12),
                0, secvert, 3, 6);

        Log.d("quad", "" + secvert);

        triangles[1] = new VertexTriangle(secvert);
    }

    public VertexQuadOfTriangles(vec3 p1, vec3 p2, vec3 p3, vec3 p4) {
        this(VertexFormatter.getVertices(p1, p2, p3, p4));
    }

    public int getShaderProgram() {
        // no shader -> GameRenderer.default
        return GameRenderer.getDefaultShaderProgram();
    }

    @Override
    public void draw() {
        int program = getShaderProgram();
        GLES20.glUseProgram(program);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(program, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        for (VertexTriangle t : triangles) {
            GLES20.glVertexAttribPointer(vertexPositionAttribute, 3, GLES20.GL_FLOAT, false, 0, t.getVertexBuffer());
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, t.getVertices().length / 3);
        }

        //GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, orderBuffer);
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, drawOrder.length);
        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);

        Log.d("quad", "DRAWING");
    }

}
