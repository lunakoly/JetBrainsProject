package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.VertexTriangle;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene2;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.MeshFactory;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;

/**
 * Created with love by iMac on 18.06.17.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "renderer";

    private static ShaderProgram defaultShaderProgram;
    private static ShaderProgram textureShaderProgram;

    private Context context;
    private Scene2 targetScene;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];


    public GameRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        initDefaultShaderProgram();
        initTextureShaderProgram();
        Log.d(TAG, "Created & shader programs have been initialized");

        Scene2 scene = new Scene2(10, 0, 0);
        SceneObject so = new SceneObject(context,
                getTextureShaderProgram(), "geography_room.png");
        scene.add(so);
        scene.cropToObject(so);

        so = new SceneObject(context, 0.5f, 0.5f, getDefaultShaderProgram());
        scene.add(so);
        
        GameRegistry.runScene(scene);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Camera c = targetScene.getCamera();
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, c.getX(), c.getY(), -3,
                c.getX(), c.getY(), 0f,
                0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        targetScene.draw(mMVPMatrix);
    }



    public static ShaderProgram getDefaultShaderProgram() {
        return defaultShaderProgram;
    }

    private void initDefaultShaderProgram() {
        defaultShaderProgram = new ShaderProgram(context, "default_vertex.vert", "default_fragment.frag");
    }

    public static ShaderProgram getTextureShaderProgram() {
        return textureShaderProgram;
    }

    private void initTextureShaderProgram() {
        textureShaderProgram = new ShaderProgram(context, "texture_vertex.vert", "texture_fragment.frag");
    }

    public void setTargetScene(Scene2 targetScene) {
        this.targetScene = targetScene;
    }

    public Scene2 getTargetScene() {
        return targetScene;
    }
}
