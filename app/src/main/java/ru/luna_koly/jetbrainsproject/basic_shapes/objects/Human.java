package ru.luna_koly.jetbrainsproject.basic_shapes.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import ru.luna_koly.jetbrainsproject.Engine;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.vec3;

/**
 * Created by user on 6/21/17.
 */

public class Human extends SceneObject {
    protected String name;


    public Human(Context context, String name, String texturePath) {
        super(context, 0.7f, 1.3f, GameRenderer.getTextureShaderProgram());
        setTexture(texturePath);
        moveY(-0.2f);
        this.name = name;
    }

    public String askName() {
        return name;
    }

    @Override
    public void notifyEvent(MotionEvent event) {
        GameSurface gs = Engine.getInstance().getSurface();
        vec3 position = getPosition();
        float aspect = getWidth() / getHeight();

        float y = event.getY() / gs.getHeight();
        float x = event.getX() / gs.getWidth();

        Log.d(name, "XY " + x + " : " + y);

        float height = getHeight() / 2;
        float width = getWidth() / 2 * aspect;

        Log.d(name, "WH/2 " + width + " : " + height);

        float dy = position.y - getScene().getCamera().getY();
        float dx = getScene().getCamera().getX() - position.x;

        Log.d(name, "DXDY " + dx + " : " + dy);

        float yMin = (1.0f - height) / 2.0f - dy / 2;
        float yMax = yMin + height;

        float xMin = (1.0f - width) / 2.0f + dx * aspect / 2.0f;
        float xMax = xMin + width;

        //Log.d(name, "CHECK " + y + " : " + yMax + " : " + yMin);
        Log.d(name, "CHECK " + x + " : " + xMax + " : " + xMin);

        if (y >  yMin && y < yMax &&
                x >  xMin && x < xMax) {
            Log.d(name, "FUCK U!!!");
        }
    }
}
