package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Camera;

/**
 * Created with love by iMac on 18.06.17.
 */

public class GameSurface extends GLSurfaceView {
    private static final String TAG = "game_view";

    private float lastX = -1;
    private float lastY = -1;


    public GameSurface(Context context) {
        super(context);
        Log.d(TAG, "Created");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (lastX == -1) {
            lastX = event.getX();
            lastY = event.getY();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            lastX = -1;
            lastY = -1;

        } else {
            Camera c = Engine.getInstance().getRenderer().getTargetScene().getCamera();

            Log.d(TAG, "" + c.getPosition().x);
            c.moveX((event.getX() - lastX) / 1000);
            c.moveY((event.getY() - lastY) / 1000);
            requestRender();
        }

        return true;
    }

}
