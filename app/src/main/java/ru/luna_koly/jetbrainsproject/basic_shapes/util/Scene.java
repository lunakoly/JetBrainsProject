package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.Shape;

/**
 * Created with love by luna_koly on 20.06.17.
 */

public class Scene {
    private float width = 1;
    private float height = 1;
    private float depth = 0;

    private Camera camera;
    private ArrayList<Shape> objects = new ArrayList<>();
    private ArrayList<Shape> UIs = new ArrayList<>();


    public Scene(float w, float h, float d) {
        this.width = w;
        this.height = h;
        this.depth = d;

        camera = new Camera(0, 0, 0);
        camera.restrictToScene(this);
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }

    float getDepth() {
        return depth;
    }

    public Camera getCamera() {
        return camera;
    }

    public Scene add(Shape object) {
        objects.add(object);
        object.setScene(this);
        return this;
    }

    public Scene addUI(Shape object) {
        UIs.add(object);
        object.setScene(this);
        return this;
    }

    public void drawObjects(float[] mvpMatrix) {
        for (Shape m : objects)
            m.draw(mvpMatrix);
    }

    public void drawUI(float[] mvpMatrix) {
        for (Shape m : UIs)
            m.draw(mvpMatrix);
    }

    public void cropToObject(SceneObject so) {
        GameSurface gs = GameRegistry.getSurface();

        this.width  = so.getWidth() * gs.getHeight() / gs.getWidth() - gs.getWidth() / gs.getHeight();
        this.height = so.getHeight() - 2;

        if (width < 0) width = -width;
        if (height < 0) height = -height;

        Log.d("HUI", "" + width + " : " + height);
    }

    public void notifyEvent(MotionEvent event) {
        for (Shape s : objects) {
            s.notifyEvent(event);
        }
    }

}
