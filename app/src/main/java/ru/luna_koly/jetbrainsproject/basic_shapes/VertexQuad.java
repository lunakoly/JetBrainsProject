package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Mesh;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by iMac on 18.06.17.
 */

public class VertexQuad implements Mesh {
    private FloatBuffer vertexBuffer;
    private float[] vertices = new float[12];

    private ShortBuffer orderBuffer;
    private short[] drawOrder = {0, 1, 2, 1, 3, 2};


    public VertexQuad(float[] vertices) {
        if (vertices.length != 12)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        bb.order(ByteOrder.nativeOrder());

        orderBuffer = bb.asShortBuffer();
        orderBuffer.put(drawOrder);
        orderBuffer.position(0);
    }

    public VertexQuad(vec3 p1, vec3 p2, vec3 p3, vec3 p4) {
        this(VertexFormatter.getVertices(p1, p2, p3, p4));
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


        GLES20.glVertexAttribPointer(vertexPositionAttribute, 4, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, orderBuffer);
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, drawOrder.length);
        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
    }
}
