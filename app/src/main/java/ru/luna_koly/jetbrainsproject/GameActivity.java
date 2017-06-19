package ru.luna_koly.jetbrainsproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends Activity {
    private static final String TAG = "game_activity";
    private GameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);     // hide title bar

        gameView = new GameSurfaceView(this);
        setContentView(gameView);

        Log.d(TAG, "Created");
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
