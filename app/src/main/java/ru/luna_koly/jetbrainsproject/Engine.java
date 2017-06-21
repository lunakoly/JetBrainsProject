package ru.luna_koly.jetbrainsproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;

/**
 * Created with love by luna_koly on 20.06.17.
 */

public class Engine {
    private static final String TAG = "engine";

    private Context context;
    private GameSurface surface;
    private GameRenderer renderer;


    Engine(Context context) {
        this.context = context;

        surface = new GameSurface(context);
        surface.setEngine(this);

        // check capability
        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (!supportsEs2) {
            Log.e(TAG, "GLES20 is not supported");
            return;
        }

        surface.setEGLContextClientVersion(2);
        Log.d(TAG, "GLES20 : OK");

        renderer = new GameRenderer(context);
        surface.setRenderer(renderer);

        new GameRegistry(this);
    }

    GameSurface getSurface() {
        return surface;
    }

    GameRenderer getRenderer() {
        return renderer;
    }

    public Context getContext() {
        return context;
    }
}
