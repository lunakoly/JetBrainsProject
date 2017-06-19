package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by luna_koly on 18.06.17.
 */

public class VertexTriangle implements Shape {
    private FloatBuffer vertexBuffer;

    /** static absolute position values */
    private float[] vertices = new float[9];
    /** dynamic values that depend on scene */
    private float[] relative_vertices = new float[9];

    private int shaderProgram = -1;


    public VertexTriangle(float[] vertices) {
        if (vertices.length != 9)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        resetRelativeVertices();
        genBuffer();

        // set to default
        shaderProgram = GameRenderer.getDefaultShaderProgram();
    }

    public VertexTriangle(vec3 p1, vec3 p2, vec3 p3) {
        this(VertexFormatter.getVertices(p1, p2, p3));
    }

    public void setShaderProgram(int program) {
        shaderProgram = program;
    }

    private void genBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(relative_vertices.length * 4); // float size
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(relative_vertices);
        vertexBuffer.position(0);
    }

    @Override
    public void resetRelativeVertices() {
        System.arraycopy(vertices, 0, relative_vertices, 0, vertices.length);
    }

    @Override
    public void rescale(vec3 center, float power) {
        relative_vertices = VertexFormatter.zoom2D(center, power, relative_vertices);
    }

    @Override
    public void recalculateVertices(Camera camera) {
        relative_vertices = VertexFormatter.projectToCamera(camera, relative_vertices);
    }

    @Override
    public void externalDraw(int vertexPositionAttribute) {
        genBuffer();
        GLES20.glVertexAttribPointer(vertexPositionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, relative_vertices.length / 3);
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
