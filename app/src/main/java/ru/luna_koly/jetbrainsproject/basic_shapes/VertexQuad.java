package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by iMac on 18.06.17.
 */

public class VertexQuad implements Shape {
    private static final short[] drawOrder = {0, 1, 2, 2, 3, 0};

    private FloatBuffer vertexBuffer;
    private float[] vertices = new float[12];

    private int shaderProgram = -1;


    public VertexQuad(float[] vertices) {
        if (vertices.length != 12)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        this.vertices = VertexFormatter.getTriadOfPattern(vertices, drawOrder);
        genBuffer();

        // set to default
        shaderProgram = GameRenderer.getDefaultShaderProgram();
    }

    public VertexQuad(vec3 p1, vec3 p2, vec3 p3, vec3 p4) {
        this(VertexFormatter.getVertices(p1, p2, p3, p4));
    }

    public void setShaderProgram(int program) {
        shaderProgram = program;
    }

    private void genBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4); // float size
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    @Override
    public void recalculateVertices(Camera camera) {
        vertices = VertexFormatter.projectToCamera2D(camera, vertices);
        genBuffer();
    }

    @Override
    public void externalDraw(int vertexPositionAttribute) {
        GLES20.glVertexAttribPointer(vertexPositionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, drawOrder.length);
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(shaderProgram);

        Log.d("quad", "DRAWN " + Arrays.toString(vertices));

        int vertexPositionAttribute = GLES20.glGetAttribLocation(shaderProgram, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        externalDraw(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);

        Log.d("quad", "DRAWN_2");
    }
}
