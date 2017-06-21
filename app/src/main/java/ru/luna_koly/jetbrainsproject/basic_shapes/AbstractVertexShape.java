package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.opengl.GLES20;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;

/**
 * Created with love by luna_koly on 19.06.17.
 */

abstract class AbstractVertexShape implements Shape {
    private FloatBuffer vertexBuffer;
    float[] vertices;
    int shaderProgram = -1;
    private Scene scene;


    void setShaderProgram(int program) {
        shaderProgram = program;
    }

    void genBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4); // float size
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }


    void addVertexAttribPointer(int vertexAttribPointer) {
        GLES20.glVertexAttribPointer(vertexAttribPointer, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
    }


    void externalDraw(int vertexPositionAttribute) {
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
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    Scene getScene() {
        return scene;
    }
}
