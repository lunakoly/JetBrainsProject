package ru.luna_koly.jetbrainsproject.basic_shapes;

import android.view.MotionEvent;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;

/**
 * Created with love by iMac on 18.06.17.
 */

public interface Shape {
    void draw(float[] mvpMatrix);
    void notifyEvent(MotionEvent event);
    void setScene(Scene scene);
}
