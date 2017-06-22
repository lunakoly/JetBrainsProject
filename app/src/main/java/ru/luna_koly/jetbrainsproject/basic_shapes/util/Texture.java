package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import ru.luna_koly.jetbrainsproject.util.FileLoader;

/**
 * Created with love by luna_koly on 23.06.17.
 */

public class Texture {
    private Bitmap bitmap;


    public Texture(Context context, String path) {
        this.bitmap = FileLoader.readBitmap(context, path);
    }

    public void updateTexture(int handler) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handler);
    }
}
