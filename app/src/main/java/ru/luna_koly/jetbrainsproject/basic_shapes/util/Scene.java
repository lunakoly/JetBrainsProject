package ru.luna_koly.jetbrainsproject.basic_shapes.util;

import android.content.Context;
import android.view.MotionEvent;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameSurface;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.Shape;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Human;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Player;
import ru.luna_koly.jetbrainsproject.basic_shapes.ui.StatusBar;
import ru.luna_koly.jetbrainsproject.graph.Graph;
import ru.luna_koly.jetbrainsproject.util.Uniforms;

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
        SceneObject so = new SceneObject(context, shaderProgram, backgroundPath);
        add(so);
        cropToObject(so);
    }

    private void addPlayerDate() {
        authority = new StatusBar(context);
        adequacy = new StatusBar(context, 0, -0.09f);
        player = new Player(context);

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
        for (Shape m : objects)
            m.draw(uniforms);

        player.draw(uniforms);
    }

    public void drawUI(Uniforms uniforms) {
        for (Shape m : UIs)
            m.draw(uniforms);
    }

    public void cropToObject(SceneObject so) {
        GameSurface gs = GameRegistry.getSurface();

        this.width  = so.getWidth() * gs.getHeight() / gs.getWidth() - gs.getWidth() / gs.getHeight();
        this.height = so.getHeight() - 2;

        if (width < 0) width = -width;
        if (height < 0) height = -height;
    }

    public void notifyEvent(MotionEvent event) {
        for (Shape s : objects) {
            s.notifyEvent(event);
        }
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

    public Graph getGraph() {
        return graph;
    }

    public Player getPlayer() {
        return player;
    }
}
