package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import ru.luna_koly.jetbrainsproject.util.FileLoader;

/**
 * Created with love by luna_koly on 23.06.17.
 */

public class Texture {
    private int[] frames;
    private Bitmap bitmap;
    private long duration;
    private int width;
    private int height;


    public Texture(Context context, String path, int framesAmount, long duration) {
        this.bitmap = FileLoader.readBitmap(context, path);
        this.duration = duration;

        Bitmap[] frames = new Bitmap[framesAmount];
        this.frames = new int[framesAmount];
        this.width  = bitmap.getWidth() / framesAmount;
        this.height = bitmap.getHeight();

        for (int i = 0; i < framesAmount; i++) {
            frames[i] = Bitmap.createBitmap(this.bitmap, i * width, 0, width, height);
            this.frames[i] = FileLoader.bitmapToTexture(frames[i]);
        }
    }

    public void updateTexture() {
//        if (frames.length == 1) {
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handler);
//            //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
//            return;
//        }
//
//        long time = System.currentTimeMillis();
//        time %= duration;
//
//        float status = (float) time / duration;
//        Bitmap frame = frames[(int) Math.floor(status * frames.length)];
//
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handler);
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, frame, 0);
    }

    public int getHandler() {
        if (frames.length == 1) {
            return frames[0];
        }


        long time = System.currentTimeMillis();
        time %= duration;

        float status = (float) time / duration;
        return frames[(int) Math.floor(status * frames.length)];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
