package ru.luna_koly.jetbrainsproject;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.luna_koly.jetbrainsproject.basic_shapes.Triangle;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Mesh;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.MeshFactory;

/**
 * Created with love by iMac on 18.06.17.
 */

public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String defaultVertexShaderCode =
            "attribute vec4 aVertexPosition;" +
                    "void main(void) {" +
                    "  gl_Position = aVertexPosition;" +
                    "}";

    private static final String defaultFragmentShaderCode =
            "precision mediump float;" +
                    "void main(void) {" +
                    "  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
                    "}";

    private static int defaultProgram;

    private ArrayList<Mesh> objects = new ArrayList<>();


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        initDefaultShaderProgram();

        objects.add(MeshFactory.getExampleTriangle());
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT /*| GLES20.GL_DEPTH_BUFFER_BIT*/);
        Log.d("triangle", "CYCLE ");

        for (Mesh m : objects)
            m.draw();
    }

    public static int getDefaultShaderProgram() {
        return defaultProgram;
    }

    public static int loadShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);

        Log.e("triangle", "---");
        Log.e("triangle", GLES20.glGetShaderInfoLog(shader));

        return shader;
    }

    public static int loadProgram(int vertexShader, int fragmentShader) {
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glLinkProgram(program);

        Log.e("triangle", "+++");
        Log.e("triangle", GLES20.glGetProgramInfoLog(program));

        return program;
    }

    private int initDefaultShaderProgram() {
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, defaultFragmentShaderCode);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, defaultVertexShaderCode);
        return loadProgram(vertexShader, fragmentShader);
    }
}
