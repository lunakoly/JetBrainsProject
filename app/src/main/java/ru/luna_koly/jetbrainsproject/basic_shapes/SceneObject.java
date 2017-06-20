package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import ru.luna_koly.jetbrainsproject.FileLoader;
import ru.luna_koly.jetbrainsproject.GameSurfaceView;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created by user on 6/19/17.
 */

public class SceneObject implements Shape {
    private static final String SEARCH_PATH = "textures/";
    private static final String TAG = "scene_object";

    private VertexRectangle rect;
    private vec3 position;

    private Context context;
    private float width, height;
    private ShaderProgram shaderProgram;
    private int program;
    private int texture;
    private Bitmap bitmap;


    public SceneObject(Context context, ShaderProgram program, String texturePath) {
        this.context = context;
        this.shaderProgram = program;
        this.program = shaderProgram.getCurrentProgram();

        this.texture = loadTexture(context, texturePath);
        this.position = new vec3(0, 0, 0);

        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();

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

    public int loadTexture(Context context, String path)
    {
        int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            bitmap = FileLoader.readBitmap(context, SEARCH_PATH + path);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        } else {
            Log.e(TAG, "Error loading texture.");
        }

        return textureHandle[0];
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(program);

        int vertexPositionAttribute = GLES20.glGetAttribLocation(program, "aVertexPosition");
        GLES20.glEnableVertexAttribArray(vertexPositionAttribute);

        int texturePositionAttribute = GLES20.glGetAttribLocation(program, "aTextureCoord");
        GLES20.glEnableVertexAttribArray(texturePositionAttribute);

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        int textureHandler = GLES20.glGetUniformLocation(program, "uTextureCoord");
        GLES20.glUniform1i(textureHandler, 0);

        // pass dimensions
        int uDimensions = GLES20.glGetUniformLocation(program, "uDimensions");
        GLES20.glUniform2f(uDimensions, this.width, this.height);

        // pass screen
        int uScreen = GLES20.glGetUniformLocation(program, "uScreen");
        GLES20.glUniform2f(uScreen, GameSurfaceView.instance.getWidth(), GameSurfaceView.instance.getHeight());

        rect.setVertexAttributePointer(texturePositionAttribute);
        rect.externalDraw(vertexPositionAttribute);

        GLES20.glDisableVertexAttribArray(vertexPositionAttribute);
        GLES20.glDisableVertexAttribArray(texturePositionAttribute);
    }

    @Override
    public void externalDraw(int vertexPositionAttribute) {}

    @Override
    public void recalculateVertices(Camera camera) {
        rect.recalculateVertices(camera);
    }

    @Override
    public void rescale(vec3 center, float power) {}

    @Override
    public void resetRelativeVertices() {}
}
