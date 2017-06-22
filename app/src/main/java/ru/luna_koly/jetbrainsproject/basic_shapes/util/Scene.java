package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Human;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Player;
import ru.luna_koly.jetbrainsproject.basic_shapes.ui.StatusBar;
import ru.luna_koly.jetbrainsproject.graph.Graph;
import ru.luna_koly.jetbrainsproject.graph.GraphVertex;
import ru.luna_koly.jetbrainsproject.graph.TrajectoryItem;
import ru.luna_koly.jetbrainsproject.util.Uniforms;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;
import ru.luna_koly.jetbrainsproject.util.containers.vec3;

/**
 * Created with love by luna_koly on 20.06.17.
 */

public class Scene {
    private float width = 1;
    private float height = 1;
    private float depth = 0;

    private Context context;
    private Camera camera;
    private Graph graph = null;
    private Player player;
    private StatusBar authority;
    private StatusBar adequacy;

    private ArrayList<Shape> objects = new ArrayList<>();
    private ArrayList<Shape> UIs = new ArrayList<>();


    public Scene(Context context, float w, float h, float d) {
        this.context = context;
        this.width = w;
        this.height = h;
        this.depth = d;

        camera = new Camera(0, 0, 0);
        camera.restrictToScene(this);
        addPlayerDate();
    }

    public Scene(Context context, ShaderProgram shaderProgram, String backgroundPath) {
        this(context, 0, 0, 0);
        SceneObject so = new SceneObject(context, shaderProgram, new Texture(context, backgroundPath, 1, 1000));
        add(so);
        cropToObject(so);
    }

    private void addPlayerDate() {
        authority = new StatusBar(context);
        adequacy = new StatusBar(context, 0, -0.09f);
        player = new Player(context);
        player.setMovingTextures(
                new Texture(context, "char/charly_moving_right.png", 2, 500),
                new Texture(context, "char/charly_moving_left.png",  2, 500)
        );

        addUI(authority);
        addUI(adequacy);
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

    public void drawObjects(Uniforms uniforms) {
        try {
            for (Shape m : objects)
                m.draw(uniforms);

            player.draw(uniforms);
        } catch (ConcurrentModificationException e) {}
    }

    public void drawUI(Uniforms uniforms) {
        try {
            for (Shape m : UIs)
                m.draw(uniforms);
        } catch (ConcurrentModificationException e) {}
    }

    public void cropToObject(SceneObject so) {
        GameSurface gs = GameRegistry.getSurface();

        this.width  = so.getWidth() * gs.getHeight() / gs.getWidth() - gs.getWidth() / gs.getHeight();
        this.height = so.getHeight() - 2;

        if (width < 0) width = -width;
        if (height < 0) height = -height;
    }

    public void putPlayer(vec2 pos) {
        player.setPosition(new vec3(pos.x, pos.y, 0));
    }

    public void notifyEvent(MotionEvent event) {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (objects.get(i).notifyEvent(event))
                return;
        }

        GameSurface s = GameRegistry.getSurface();
        float y = event.getY() / s.getHeight() * 2.0f - 1;
        float x = (event.getX() / s.getWidth() * 2.0f - 1) * s.getWidth() / s.getHeight();

        x -= camera.getX();
        y += camera.getY();

        GraphVertex destination = graph.findNearestGraphPoint(new vec2(x, y));
        vec2 playerPos = new vec2(-player.getPosition().x, player.getPosition().y);
        GraphVertex playerNearest = graph.findNearestGraphPoint(playerPos);

        TrajectoryItem t = graph.getPath(playerNearest, destination);
        player.setTrajectoryItem(t);
    }

    private SceneObject mark(vec2 pos) {
        SceneObject so = new SceneObject(context, 0.2f, 0.2f, GameRenderer.getDefaultShaderProgram());
        so.moveX(pos.x);
        so.moveY(pos.y);
        add(so);
        return so;
    }

    public ArrayList<Shape> getObjects() {
        return objects;
    }

    public Human findHuman(String name) {
        for (Shape h : objects)
            if (h instanceof Human)
                if (((Human) h).askName().equals(name))
                    return (Human) h;

        return null;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void lightGraph() {
        String[] keys = new String[0];
        keys = graph.keySet().toArray(keys);

        for (int i = 0; i < keys.length; i++)
            mark(graph.get(keys[i]).position);
    }

    public Graph getGraph() {
        return graph;
    }

    public Player getPlayer() {
        return player;
    }
}
