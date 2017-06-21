package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;
import ru.luna_koly.jetbrainsproject.util.Uniforms;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;

/**
 * Created with love by luna_koly on 18.06.17.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "renderer";

    private static HashMap<String, ShaderProgram> shaderPrograms = new HashMap<>();

    private Context context;
    private Scene targetScene;
    private Engine engine;


    // mMVPDynamicMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPDynamicMatrix = new float[16];
    private final float[] mMVPStaticMatrix  = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mDynamicViewMatrix = new float[16];
    private final float[] mStaticViewMatrix  = new float[16];


    GameRenderer(Context context) {
        this.context = context;
    }

    void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        initShaderPrograms();
        Log.d(TAG, "Created & shader programs have been initialized");

        GameRegistry.runStartupAlgorithms();
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
        Uniforms u = new Uniforms();

        u.globalTime = System.currentTimeMillis() % 1000;
        u.screen = new vec2(engine.getSurface().getWidth(), engine.getSurface().getHeight());

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mDynamicViewMatrix, 0, c.getX(), c.getY(), -3,
                c.getX(), c.getY(), 0f,
                0f, 1.0f, 0.0f);
        Matrix.setLookAtM(mStaticViewMatrix, 0, 0, 0, -3,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPDynamicMatrix, 0, mProjectionMatrix, 0, mDynamicViewMatrix, 0);
        Matrix.multiplyMM(mMVPStaticMatrix, 0, mProjectionMatrix, 0, mStaticViewMatrix, 0);

        u.mvpMatrix = mMVPDynamicMatrix;

        targetScene.drawObjects(u);
        u.mvpMatrix = mMVPStaticMatrix;
        targetScene.drawUI(u);
    }

    private void initShaderPrograms() {
        shaderPrograms.put("default", new ShaderProgram(context, "default_vertex.vert", "default_fragment.frag"));
        shaderPrograms.put("texture", new ShaderProgram(context, "texture_vertex.vert", "texture_fragment.frag"));
        shaderPrograms.put("blink",   new ShaderProgram(context, "texture_vertex.vert", "blink_fragment.frag"));
    }

    public static ShaderProgram getDefaultShaderProgram() {
        return shaderPrograms.get("default");
    }

    public static ShaderProgram getTextureShaderProgram() {
        return shaderPrograms.get("texture");
    }

    void setTargetScene(Scene targetScene) {
        this.targetScene = targetScene;
    }

    Scene getTargetScene() {
        return targetScene;
    }

    public static ShaderProgram getShaderProgram(String shaderId) {
        return shaderPrograms.get(shaderId);
    }
}
