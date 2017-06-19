package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public abstract class AbstractVertexShape implements Shape {
    protected FloatBuffer vertexBuffer;

    /** static absolute position values */
    protected float[] vertices;
    /** dynamic values that depend on scene */
    protected float[] relative_vertices;

    protected int shaderProgram = -1;


    public void setShaderProgram(int program) {
        shaderProgram = program;
    }

    protected void genBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(relative_vertices.length * 4); // float size
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(relative_vertices);
        vertexBuffer.position(0);
    }


    @Override
    public void externalDraw(int vertexPositionAttribute) {
        genBuffer();
        GLES20.glVertexAttribPointer(vertexPositionAttribute, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, relative_vertices.length / 3);
    }

    @Override
    public void recalculateVertices(Camera camera) {
        relative_vertices = VertexFormatter.projectToCamera(camera, relative_vertices);
    }

    @Override
    public void rescale(vec3 center, float power) {
        relative_vertices = VertexFormatter.zoom2D(center, power, relative_vertices);
    }

    @Override
    public void resetRelativeVertices() {
        relative_vertices = new float[vertices.length];
        System.arraycopy(vertices, 0, relative_vertices, 0, vertices.length);
    }

}
