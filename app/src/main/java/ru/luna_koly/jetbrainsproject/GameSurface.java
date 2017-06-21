package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;

/**
 * Created with love by luna_koly on 18.06.17.
 */

public class GameSurface extends GLSurfaceView {
    private static final String TAG = "game_view";
    private static final long CLICK_DURATION = 200;

    private Engine engine;
    private float lastX = -1;
    private float lastY = -1;
    private long pressDuration;


    public GameSurface(Context context) {
        super(context);
        Log.d(TAG, "Created");
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = event.getX();
            lastY = event.getY();
            pressDuration = System.currentTimeMillis();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            lastX = -1;
            lastY = -1;

            if (System.currentTimeMillis() - pressDuration < CLICK_DURATION)
                click(event);

        } else {
            Camera c = engine.getRenderer().getTargetScene().getCamera();
            c.moveX((event.getX() - lastX) / 1000);
            c.moveY((event.getY() - lastY) / 1000);
            requestRender();
        }

        return true;
    }

    private void click(MotionEvent event) {
        engine.getRenderer().getTargetScene().notifyEvent(event);
    }
}
