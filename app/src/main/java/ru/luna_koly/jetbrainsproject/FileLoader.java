package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLockInterruptionException;
import java.util.Scanner;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class FileLoader {
    private static final String TAG = "file_loader";
    private static final String TEXTURES_PATH = "textures/";

    private static String readStream(InputStream is) {
        Scanner scn = new Scanner(is);
        String out = "";

        while (scn.hasNextLine()) {
            out += scn.nextLine();
        }

        return out;
    }

    private static InputStream streamFile(Context context, String path) {
        try {
            return context.getAssets().open(path);
        } catch (IOException e) {
            Log.d(TAG, "Failed loading file at \'" + path + "\'");
            e.printStackTrace();
        }

        return null;
    }

    public static String readFile(Context context, String path) {
        return readStream(streamFile(context, path));
    }

    public static Bitmap readBitmap(Context context, String path) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(TEXTURES_PATH + path));
        } catch (IOException e) {
            Log.e(TAG, "Unable to load file " + path);
            return null;
        }
    }

    public static int bitmapToTexture(Bitmap bitmap) {
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

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

    public int loadTexture(Context context, String path) {
        Bitmap bitmap = FileLoader.readBitmap(context, TEXTURES_PATH + path);
        return bitmapToTexture(bitmap);
    }

}
