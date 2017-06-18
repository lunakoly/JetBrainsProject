package ru.luna_koly.jetbrainsproject;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends Activity {

    private GameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameSurfaceView(this);
        setContentView(gameView);
    }

}
