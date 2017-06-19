package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by iMac on 18.06.17.
 */

public class VertexTriangle implements Shape {
    private FloatBuffer vertexBuffer;
    private float[] vertices = new float[9];

    public VertexTriangle(float[] vertices) {
        if (vertices.length != 9)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        Log.d("quad", "" + Arrays.toString(vertices));

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public VertexTriangle(vec3 p1, vec3 p2, vec3 p3) {
        this(VertexFormatter.getVertices(p1, p2, p3));
//        vertices = VertexFormatter.getVertices(p1, p2, p3);
//
//        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//
//        vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(vertices);
//        vertexBuffer.position(0);
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public float[] getVertices() {
        return vertices;
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

        GLES20.glVertexAttribPointer(vertexPositionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);
        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
    }
}
