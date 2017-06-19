package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.MeshFactory;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.ShaderProgram;

/**
 * Created with love by iMac on 18.06.17.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    final static private String tag = "renderer";

    private static ShaderProgram defaultShaderProgram;

    private Context context;
    Camera camera = new Camera(0, 0, 0);
    private ArrayList<Shape> objects = new ArrayList<>();


    public GameRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        initDefaultShaderProgram();
        Log.d(tag, "Created & default shader program has been initialized");

        objects.add(MeshFactory.getExampleTriangle());
        objects.add(MeshFactory.getExampleRectangle());

        camera.moveX(0.5f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for (Shape m : objects)
            m.recalculateVertices(camera);

        Log.d(tag, "DRAWING");

        for (Shape m : objects)
            m.draw();
    }

    public static int getDefaultShaderProgram() {
        return defaultShaderProgram.getCurrentProgram();
    }

    private void initDefaultShaderProgram() {
        defaultShaderProgram = new ShaderProgram(context, "default_vertex.vert", "default_fragment.frag");
    }
}
