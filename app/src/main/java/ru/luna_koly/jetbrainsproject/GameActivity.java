package ru.luna_koly.jetbrainsproject;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import ru.luna_koly.jetbrainsproject.util.LoadingInitializer;

public class GameActivity extends Activity {
    private static final String TAG = "game_activity";

    private GLSurfaceView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);     // hide title bar

        Engine engine = new Engine(this);
        gameView = engine.getSurface();
        setContentView(gameView);

        Log.d(TAG, "Created");

        GameRegistry.addStartupAlgorithm(new LoadingInitializer(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }
}
