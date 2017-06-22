package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;
import ru.luna_koly.jetbrainsproject.util.FileLoader;
import ru.luna_koly.jetbrainsproject.util.Uniforms;
import ru.luna_koly.jetbrainsproject.util.VertexFormatter;
import ru.luna_koly.jetbrainsproject.util.containers.vec3;

/**
 * Created with love by luna_koly on 6/19/17.
 */

public class SceneObject implements Shape {
    private static final String TAG = "scene_object";

    private VertexRectangle rect;
    private vec3 position;

    private Context context;
    private float width, height;
    protected int program;
    private int texture;
    private Bitmap bitmap;
    private boolean positionChanged;


    public SceneObject(Context context, ShaderProgram program, String texturePath) {
        this.context = context;
        this.program = program.getCurrentProgram();

        this.bitmap = FileLoader.readBitmap(context, texturePath);
        this.texture = FileLoader.bitmapToTexture(bitmap);
        this.position = new vec3(0, 0, 0);

        setSize(2 * bitmap.getWidth() / (float) bitmap.getHeight(), 2);
    }

    public SceneObject(Context context, float width, float height, ShaderProgram program) {
        this.context = context;
        this.program = program.getCurrentProgram();

        this.position = new vec3(0, 0, 0);

        setSize(width, height);
    }

    public void setSize(float width, float height) {
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

        rect = new VertexRectangle(vertices);
        rect.setShaderProgram(this.program);
    }

    public void setTexture(String texturePath) {
        this.bitmap = FileLoader.readBitmap(context, texturePath);
        this.texture = FileLoader.bitmapToTexture(bitmap);
    }

    public void setPosition(vec3 position) {
        this.position = new vec3(-position.x, position.y, position.z);
        positionChanged = true;
    }

    public vec3 getPosition() {
        return position;
    }

    public void moveX(float f) {
        position = new vec3(position.x - f, position.y, position.z);
        positionChanged = true;
    }

    public void moveY(float f) {
        position = new vec3(position.x, position.y + f, position.z);
        positionChanged = true;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    protected void externalDraw(int vertexPositionAttribute, int texturePositionAttribute, Uniforms u) {
        // pass dimensions
        int uDimensions = GLES20.glGetUniformLocation(program, "uDimensions");
        GLES20.glUniform2f(uDimensions, this.width, this.height);

        // pass screen
        int uScreen = GLES20.glGetUniformLocation(program, "uScreen");
        GLES20.glUniform2f(uScreen, u.screen.x, u.screen.y);

        // pass dimensions
        int uPosition = GLES20.glGetUniformLocation(program, "uPosition");
        GLES20.glUniform2f(uPosition, position.x, position.y);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        int textureHandler = GLES20.glGetUniformLocation(program, "uTextureCoord");
        GLES20.glUniform1i(textureHandler, 0);

        int uMVPMatrix = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, u.mvpMatrix, 0);

        // global time
        int uGlobalTime = GLES20.glGetUniformLocation(program, "uGlobalTime");
        GLES20.glUniform1f(uGlobalTime, u.globalTime);


        // update pos
        if (positionChanged) {
            rect.vertices = VertexFormatter.translateToPosition(position, rect.vertices);
            rect.genBuffer();
            positionChanged = false;
        }

        rect.addVertexAttribPointer(texturePositionAttribute);
        rect.externalDraw(vertexPositionAttribute);
    }

    @Override
    public void draw(Uniforms u) {
        GLES20.glUseProgram(program);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(program, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        int texturePositionAttribute = GLES20.glGetAttribLocation(program, "aTextureCoord");
        GLES20.glEnableVertexAttribArray(texturePositionAttribute);

        externalDraw(vertexPositionAttribute, texturePositionAttribute, u);

        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(texturePositionAttribute);
    }

    @Override
    public void notifyEvent(MotionEvent event) {

    }

    @Override
    public void setScene(Scene scene) {
        rect.setScene(scene);
    }

    protected Scene getScene() {
        return rect.getScene();
    }

    public void setShaderProgram(ShaderProgram program) {
        this.program = program.getCurrentProgram();
        rect.setShaderProgram(this.program);
    }

    public Context getContext() {
        return context;
    }
}
