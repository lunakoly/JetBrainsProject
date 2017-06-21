package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import ru.luna_koly.jetbrainsproject.util.FileLoader;

/**
 * Created with love by luna_koly on 6/19/17.
 */

public class ShaderProgram {
    private static final String TAG = "shader_object";

    private String fragmentShaderSource = "";
    private String vertexShaderSource = "";
    private int program = -1;


    public ShaderProgram(Context context, String vertexPath, String fragmentPath) {
        fragmentShaderSource = FileLoader.readShaderSource(context, fragmentPath);
        vertexShaderSource   = FileLoader.readShaderSource(context, vertexPath);

        if (fragmentShaderSource.isEmpty()) Log.e(TAG, "Error while reading fragment shader file");
        if (vertexShaderSource.isEmpty()) Log.e(TAG, "Error while reading fragment shader file");
    }

    private void linkAll() {
        int fragmentShader = FileLoader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        int vertexShader = FileLoader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        program = FileLoader.loadProgram(vertexShader, fragmentShader);
    }

    public int getCurrentProgram() {
        if (program == -1)
            linkAll();

        return program;
    }
}
