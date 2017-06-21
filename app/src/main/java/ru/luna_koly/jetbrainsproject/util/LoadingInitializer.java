package ru.luna_koly.jetbrainsproject.util;

import android.content.Context;
import android.util.Log;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.Actionable;
import ru.luna_koly.jetbrainsproject.basic_shapes.BasicActivator;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Human;
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
        Scene scene = FileLoader.loadScene(context, "entering_hall");
        Human m = scene.findHuman("Mr. Mitozhirski");
        if (m != null)
            m.addActivationResult(new BasicActivator() {
                @Override
                public void activate() {
                    Log.d(TAG, "CHLEN");
                }
            });

        GameRegistry.runScene(scene);
    }

    public static void loadData() {

    }

    public static void clearData() {

    }
}
