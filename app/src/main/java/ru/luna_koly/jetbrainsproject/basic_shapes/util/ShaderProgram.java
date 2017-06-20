package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import ru.luna_koly.jetbrainsproject.FileLoader;

/**
 * Created with love by luna_koly on 6/19/17.
 */

public class ShaderProgram {
    private static final String TAG = "shader_object";
    private static final String SEARCH_PATH = "shader/";

    private String fragmentShaderSource = "";
    private String vertexShaderSource = "";

    private int fragmentShader  = -1;
    private int vertexShader    = -1;
    private int program         = -1;


    public ShaderProgram(Context context, String vertexPath, String fragmentPath) {
        fragmentShaderSource = FileLoader.readFile(context, SEARCH_PATH + fragmentPath);
        vertexShaderSource   = FileLoader.readFile(context, SEARCH_PATH + vertexPath);

        if (fragmentShaderSource.isEmpty()) Log.e(TAG, "Error while reading fragment shader file");
        if (vertexShaderSource.isEmpty()) Log.e(TAG, "Error while reading fragment shader file");
    }

    public int linkAll() {
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        program = loadProgram(vertexShader, fragmentShader);
        return program;
    }

    public int getCurrentProgram() {
        if (program == -1)
            linkAll();

        return program;
    }


    public static int loadShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);

        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);

        if (status[0] == 0) {
            Log.e(TAG, "Couldn't load " + type);
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0)
            Log.e(TAG, "Loading shader trouble");

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
            Log.e(TAG, "Couldn't load shader program");
            Log.e(TAG, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
        }

        if (program == 0)
            Log.e(TAG, "Loading program trouble");

        return program;
    }

}
