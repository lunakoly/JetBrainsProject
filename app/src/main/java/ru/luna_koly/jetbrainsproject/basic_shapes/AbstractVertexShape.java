package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene2;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public abstract class AbstractVertexShape implements Shape {
    protected FloatBuffer vertexBuffer;
    protected float[] vertices;
    protected int shaderProgram = -1;
    private Scene2 scene;


    public void setShaderProgram(int program) {
        shaderProgram = program;
    }

    protected void genBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4); // float size
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }


    public void addVertexAttribPointer(int vertexAttribPointer) {
        GLES20.glVertexAttribPointer(vertexAttribPointer, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    }


    public void externalDraw(int vertexPositionAttribute) {
        addVertexAttribPointer(vertexPositionAttribute);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(shaderProgram);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(shaderProgram, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        int uMVPMatrix = GLES20.glGetUniformLocation(shaderProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, mvpMatrix, 0);

        externalDraw(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
    }

    @Override
    public void notifyEvent(MotionEvent event) {}


    @Override
    public void setScene(Scene2 scene) {
        this.scene = scene;
    }

    public Scene2 getScene() {
        return scene;
    }
}
