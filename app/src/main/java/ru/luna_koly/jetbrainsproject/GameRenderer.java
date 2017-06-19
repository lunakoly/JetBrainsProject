package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
    private ArrayList<Shape> objects = new ArrayList<>();


    public GameRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        initDefaultShaderProgram();
        Log.d(tag, "Created & default shader program has been initialized");

        //objects.add(MeshFactory.getExampleTriangle());
        objects.add(MeshFactory.getExampleRectangle());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT /*| GLES20.GL_DEPTH_BUFFER_BIT*/);

        for (Shape m : objects)
            m.draw();
    }

    public static int getDefaultShaderProgram() {
        return defaultShaderProgram.getCurrentProgram();
    }

    public static int loadShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);

        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);

        if (status[0] == 0) {
            Log.e(tag, "Couldn't load " + type);
            Log.e(tag, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0)
            Log.e(tag, "Loading shader trouble");

        return shader;
    }

    public static int loadProgram(int vertexShader, int fragmentShader) {
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glLinkProgram(program);

        int[] status = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);

        if (status[0] == 0) {
            Log.e(tag, "Couldn't load shader program");
            Log.e(tag, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
        }

        if (program == 0)
            Log.e(tag, "Loading program trouble");

        return program;
    }

    private void initDefaultShaderProgram() {
        defaultShaderProgram = new ShaderProgram(context, "default_vertex.vert", "default_fragment.frag");
    }
}
