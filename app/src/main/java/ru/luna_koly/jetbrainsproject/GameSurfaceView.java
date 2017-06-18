package ru.luna_koly.jetbrainsproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * Created with love by iMac on 18.06.17.
 */

public class GameSurfaceView extends GLSurfaceView {

    private GameRenderer renderer;

    public GameSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        Log.d("triangle", "" + supportsEs2);

        renderer = new GameRenderer();
        setRenderer(renderer);

        // render only if there're some changes
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
