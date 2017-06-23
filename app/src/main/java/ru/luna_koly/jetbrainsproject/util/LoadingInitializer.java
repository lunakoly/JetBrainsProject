package ru.luna_koly.jetbrainsproject.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Player;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;

/**
 * Created with love by luna_koly on 6/21/17.
 */

public class LoadingInitializer implements Runnable {
    private static final String TAG = "loading_manager";

    private Context context;


    public LoadingInitializer(Context context) {
        this.context = context;
        Log.d(TAG, "Initialized");
    }

    @Override
    public void run() {
        try {
            String[] scenes = context.getAssets().list("scenes");

            for (String s : scenes) {
                Log.d(TAG, "Scene loaded: " + s);
                FileLoader.loadScene(context, s.split("\\.")[0]);     // get rid of .xml
            }

        } catch (IOException e) {
            Log.e(TAG, "run: Error, loading scene sources");
            e.printStackTrace();
        }

        Scene scene = GameRegistry.getScene("entering_hall");
        GameRegistry.runScene(scene);
        scene.putPlayer(scene.getGraph().get("0").position);
    }

    public static void loadData() {

    }

    public static void clearData() {

    }
}
