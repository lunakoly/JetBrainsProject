package ru.luna_koly.jetbrainsproject.basic_shapes.entity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.Actionable;
import ru.luna_koly.jetbrainsproject.basic_shapes.BasicActivator;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.dialogs.Dialog;
import ru.luna_koly.jetbrainsproject.dialogs.DialogActivity;
import ru.luna_koly.jetbrainsproject.util.containers.vec3;

/**
 * Created with love by luna_koly on 6/21/17.
 */

public class Human extends SceneObject implements Actionable {
    private ArrayList<Actionable> results = new ArrayList<>();

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
        GameSurface gs = GameRegistry.getSurface();
        vec3 position = getPosition();
        float aspect = getWidth() / getHeight();

        float y = event.getY() / gs.getHeight();
        float x = event.getX() / gs.getWidth();

        float height = getHeight() / 2;
        float width = getWidth() / 2 * aspect;

        float dy = position.y - getScene().getCamera().getY();
        float dx = getScene().getCamera().getX() - position.x;

        float yMin = (1.0f - height) / 2.0f - dy / 2;
        float yMax = yMin + height;

        float xMin = (1.0f - width) / 2.0f + dx * aspect / 2.0f;
        float xMax = xMin + width;

        if (y >  yMin && y < yMax &&
                x >  xMin && x < xMax) {
            activate();
        }
    }

    @Override
    public void activate() {
        for (Actionable r : results)
            r.activate();
    }

    @Override
    public void addActivationResult(Actionable result) {
        results.add(result);
    }

    public void removeActivationResult(Actionable result) {
        results.remove(result);
    }

    public void addDialog(final Dialog d) {
        addActivationResult(new BasicActivator() {
            @Override
            public void activate() {
                Intent i = new Intent(getContext(), DialogActivity.class);
                DialogActivity.setDialog(d);
                getContext().startActivity(i);
            }
        });
    }
}
