package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;

import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.Engine;
import ru.luna_koly.jetbrainsproject.FileLoader;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created by user on 6/19/17.
 */

public class SceneObject implements Shape {
    private static final String TAG = "scene_object";

    private VertexRectangle rect;
    private vec3 position;

    private Context context;
    private float width, height;
    private int program;
    private int texture;
    private Bitmap bitmap;


    public SceneObject(Context context, ShaderProgram program, String texturePath) {
        this.context = context;
        this.program = program.getCurrentProgram();

        this.bitmap = FileLoader.readBitmap(context, texturePath);
        this.texture = FileLoader.bitmapToTexture(bitmap);
        this.position = new vec3(0, 0, 0);

        this.height = 2;
        this.width = 2 * bitmap.getWidth() / (float) bitmap.getHeight();

        float w = width / 2;
        float h = height / 2;

        float[] vertices = {
                -w, +h, 0.0f,
                -w, -h, 0.0f,
                +w, -h, 0.0f,
                +w, +h, 0.0f
        };

        Log.d(TAG, "draw: " + Arrays.toString(vertices));

        rect = new VertexRectangle(vertices);
        rect.setShaderProgram(this.program);
    }

    public SceneObject(Context context, float width, float height, ShaderProgram program) {
        this.context = context;
        this.program = program.getCurrentProgram();

        this.position = new vec3(0, 0, 0);

        this.height = height;
        this.width = width;

        float w = width / 2;
        float h = height / 2;

        float[] vertices = {
                -w, +h, 0.0f,
                -w, -h, 0.0f,
                +w, -h, 0.0f,
                +w, +h, 0.0f
        };

        Log.d(TAG, "draw: " + Arrays.toString(vertices));

        rect = new VertexRectangle(vertices);
        rect.setShaderProgram(this.program);
    }

    public void setTexture(String texturePath) {
        this.bitmap = FileLoader.readBitmap(context, texturePath);
        this.texture = FileLoader.bitmapToTexture(bitmap);
    }

    public void setPosition(vec3 position) {
        this.position = position;
    }

    public void moveX(float f) {
        position = new vec3(f, position.y, position.z);
    }

    public void moveY(float f) {
        position = new vec3(position.x, f, position.z);
    }

    public void moveZ(float f) {
        position = new vec3(position.x, position.y, f);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(program, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        int texturePositionAttribute = GLES20.glGetAttribLocation(program, "aTextureCoord");
        GLES20.glEnableVertexAttribArray(texturePositionAttribute);

        // pass dimensions
        int uDimensions = GLES20.glGetUniformLocation(program, "uDimensions");
        GLES20.glUniform2f(uDimensions, this.width, this.height);

        // pass screen
        int uScreen = GLES20.glGetUniformLocation(program, "uScreen");
        GameSurface gs = Engine.getInstance().getSurface();
        GLES20.glUniform2f(uScreen, gs.getWidth(), gs.getHeight());


        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        int textureHandler = GLES20.glGetUniformLocation(program, "uTextureCoord");
        GLES20.glUniform1i(textureHandler, 0);

        int uMVPMatrix = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, mvpMatrix, 0);


        rect.addVertexAttribPointer(texturePositionAttribute);
        rect.externalDraw(vertexPositionAttribute);

        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(texturePositionAttribute);
    }

}
