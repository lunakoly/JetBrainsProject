package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.Engine;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;

/**
 * Created with love by luna_koly on 20.06.17.
 */

public class Scene2 {
    private float width = 1;
    private float height = 1;
    private float depth = 0;

    private Camera camera;
    private ArrayList<Shape> objects = new ArrayList<>();


    public Scene2(float w, float h, float d) {
        this.width = w;
        this.height = h;
        this.depth = d;

        camera = new Camera(0, 0, 0);
        camera.restrictToScene(this);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }

    public Camera getCamera() {
        return camera;
    }

    public Scene2 add(Shape object) {
        objects.add(object);
        return this;
    }

    public void draw(float[] mvpMatrix) {
        for (Shape m : objects)
            m.draw(mvpMatrix);
    }

    public void cropToObject(SceneObject so) {
        GameSurface gs = Engine.getInstance().getSurface();

        this.width  = so.getWidth() * gs.getHeight() / gs.getWidth() - gs.getWidth() / gs.getHeight();
        this.height = so.getHeight() - 2;

        if (width < 0) width = -width;
        if (height < 0) height = -height;

        Log.d("HUI", "" + width + " : " + height);
    }
}
