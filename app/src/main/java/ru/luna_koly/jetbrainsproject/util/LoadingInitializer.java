package ru.luna_koly.jetbrainsproject.util;

import android.content.Context;
import android.util.Log;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
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
        Scene scene = new Scene(10, 0, 0);
        SceneObject so = new SceneObject(context,
                GameRenderer.getTextureShaderProgram(), "entering_hall.png");
        scene.add(so);
        scene.cropToObject(so);

        so = new Human(context, "Mr. Mitozhirski", "char/mitozhirski_standing.png");
        so.moveX(-1f);
        scene.add(so);

        so = new Player(context);
        scene.add(so);

        GameRegistry.addScene(scene, "entering_hall");
        GameRegistry.runScene(GameRegistry.getScene("entering_hall"));
    }

    public static void loadData() {

    }

    public static void clearData() {

    }
}
