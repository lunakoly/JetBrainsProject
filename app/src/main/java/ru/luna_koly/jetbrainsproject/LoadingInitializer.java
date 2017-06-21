package ru.luna_koly.jetbrainsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.objects.Human;
import ru.luna_koly.jetbrainsproject.basic_shapes.objects.Player;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene2;

/**
 * Created by user on 6/21/17.
 */

public class LoadingInitializer implements Runnable {
    private static final String TAG = "loading_manager";

    private static LoadingInitializer lastInstance;

    private Context context;
    private Profile[] profiles = new Profile[2];
    private Profile current;

    public LoadingInitializer(Context context) {
        this.context = context;
        lastInstance = this;

        for (int i = 0; i < profiles.length; i++)
            profiles[i] = new Profile();

        current = profiles[0];
    }

    @Override
    public void run() {
        Scene2 scene = new Scene2(10, 0, 0);
        SceneObject so = new SceneObject(context,
                GameRenderer.getTextureShaderProgram(), "entering_hall.png");
        scene.add(so);
        scene.cropToObject(so);

        so = new Human(context, "Mr. Mitozhirski", "char/mitozhirski_standing.png");
        so.moveX(-1f);
        scene.add(so);

        so = new Player(context);
        scene.add(so);

        so.moveY(+0.5f);

        GameRegistry.runScene(scene);
    }

    public static void playProfile(Activity context, int i) {
        Intent intent = new Intent(context, GameActivity.class);
        lastInstance.current = lastInstance.profiles[i];
        context.startActivity(intent);
    }

    public static void deleteProfile(MainActivity mainActivity, int i) {
        lastInstance.profiles[i] = new Profile();
    }

    public static LoadingInitializer getInstance() {
        return lastInstance;
    }

    public Context getContext() {
        return context;
    }

    public static void update(View save) {
        TextView status = (TextView) save.findViewById(R.id.status);
        status.setText(lastInstance.profiles[0].lastChange);
    }
}
