package ru.luna_koly.jetbrainsproject.basic_shapes.ui;

import android.content.Context;
import android.opengl.GLES20;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;
import ru.luna_koly.jetbrainsproject.util.Uniforms;

/**
 * Created with love by luna_koly on 22.06.17.
 */

public class StatusBar extends SceneObject {
    private float status = 0.25f;   // [0; 1]


    public StatusBar(Context context) {
        super(context, 0.8f, 0.06f, new ShaderProgram(context, "default_vertex.vert", "status_bar_fragment.frag"));
        moveY(0.9f);
    }

    public StatusBar(Context context, float dx, float dy) {
        super(context, 0.8f, 0.06f, new ShaderProgram(context, "default_vertex.vert", "status_bar_fragment.frag"));
        moveY(0.9f + dy);
        moveX(dx);
    }

    @Override
    public void draw(Uniforms u) {
        GLES20.glUseProgram(program);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(program, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        int texturePositionAttribute = GLES20.glGetAttribLocation(program, "aTextureCoord");
        GLES20.glEnableVertexAttribArray(texturePositionAttribute);

        int uStatus = GLES20.glGetUniformLocation(program, "uStatus");
        GLES20.glUniform1f(uStatus, status);

        externalDraw(vertexPositionAttribute, texturePositionAttribute, u);

        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(texturePositionAttribute);
    }
}
