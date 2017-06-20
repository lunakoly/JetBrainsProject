package ru.luna_koly.jetbrainsproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created with love by iMac on 18.06.17.
 */

public class GameSurfaceView extends GLSurfaceView {
    private static final String TAG = "game_view";
    private GameRenderer renderer;
    private float lastX = -1;
    private float lastY = -1;

    public static GameSurfaceView instance;


    public GameSurfaceView(Context context) {
        super(context);
        instance = this;

        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (!supportsEs2) {
            Log.e(TAG, "ES2 is not supported");
            return;
        }

        setEGLContextClientVersion(2);
        Log.d(TAG, "GLES20 : OK");

        renderer = new GameRenderer(context);
        setRenderer(renderer);

        // render only if there're some changes
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        Log.d(TAG, "Game surface view : OK");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (lastX == -1) {
            lastX = event.getX();
            lastY = event.getY();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            lastX = -1;
            lastY = -1;

        } else {
            GameRenderer.camera.moveX((event.getX() - lastX) / 5000);
            GameRenderer.camera.moveY((lastY - event.getY()) / 1000);
            requestRender();
        }

        return true;
    }


}
