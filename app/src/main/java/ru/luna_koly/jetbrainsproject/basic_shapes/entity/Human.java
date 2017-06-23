package ru.luna_koly.jetbrainsproject.basic_shapes.entity;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.Actionable;
import ru.luna_koly.jetbrainsproject.basic_shapes.BasicActivator;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Texture;
import ru.luna_koly.jetbrainsproject.dialogs.Dialog;
import ru.luna_koly.jetbrainsproject.dialogs.DialogActivity;
import ru.luna_koly.jetbrainsproject.graph.TrajectoryItem;
import ru.luna_koly.jetbrainsproject.util.Uniforms;
import ru.luna_koly.jetbrainsproject.util.containers.Vector2D;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;
import ru.luna_koly.jetbrainsproject.util.containers.vec3;

/**
 * Created with love by luna_koly on 6/21/17.
 */

public class Human extends SceneObject implements Actionable {
    private static final float SPEED = 0.02f;

    private ArrayList<Actionable> results = new ArrayList<>();

    protected String name;
    private TrajectoryItem trajectoryItem = null;
    private long lastTime = 0;
    private vec2 lastPos = null;
    private Texture movingRight = null;
    private Texture movingLeft = null;
    private Texture standing = null;


    public Human(Context context, String name, Texture texture) {
        super(context, 0.7f, 1.3f, GameRenderer.getTextureShaderProgram());
        setTexture(texture);
        moveY(-0.2f);
        this.name = name;
        this.standing = texture;
    }

    public void setMovingTextures(Texture movingRight, Texture movingLeft) {
        this.movingRight = movingRight;
        this.movingLeft  = movingLeft;
    }

    public String askName() {
        return name;
    }

    @Override
    public boolean notifyEvent(MotionEvent event) {
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
            return true;
        }

        return false;
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

    public void setTrajectoryItem(TrajectoryItem trajectoryItem) {
        this.trajectoryItem = trajectoryItem;
    }

    public TrajectoryItem getTrajectoryItem() {
        return trajectoryItem;
    }

    @Override
    public void draw(Uniforms u) {
        long newTime = System.currentTimeMillis();
        if (lastTime == 0) lastTime = newTime;
        float dt = newTime - lastTime;
        lastTime = newTime;

        if (trajectoryItem != null) {
            vec2 pos = new vec2(-getPosition().x, getPosition().y);

            double angle = Vector2D.getAngleOf2Points(pos, trajectoryItem.getPosition());

            moveX((float) (SPEED * Math.cos(angle) * dt / 25f));
            moveY((float) (SPEED * Math.sin(angle) * dt / 25f));

            trajectoryItem = trajectoryItem.check(pos);
            onMove(pos);
        } else {
            onStop();
        }

        super.draw(u);
    }

    private void onStop() {
        setTexture(standing);
    }

    private void onMove(vec2 pos) {
        if (lastPos != null) {

            if (pos.x - lastPos.x > 0 && getTexture() != movingRight) {
                setTexture(movingRight);
            } else if (pos.x - lastPos.x < 0 && getTexture() != movingLeft) {
                setTexture(movingLeft);
            }

        }

        lastPos = pos;
        getScene().onPlayerMove();
    }
}
