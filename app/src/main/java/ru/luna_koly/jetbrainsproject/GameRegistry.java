package ru.luna_koly.jetbrainsproject;

import android.content.Context;

import java.util.ArrayList;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene2;

/**
 * Created with love by luna_koly on 20.06.17.
 */

public class GameRegistry {
    private static GameRegistry lastInstance;

    private Engine engine;
    private ArrayList<Scene2> scenes = new ArrayList<>();
    private Scene2 lastScene = null;


    public GameRegistry(Engine engine) {
        this.engine = engine;
        lastInstance = this;
    }


    private void registerScene(Scene2 scene) {
        scenes.add(scene);
    }

    public static GameRegistry addScene(Scene2 scene) {
        lastInstance.registerScene(scene);
        return lastInstance;
    }

    private void switchScene(Scene2 scene) {
        //closeScene(lastScene);
        engine.getRenderer().setTargetScene(scene);
        lastScene = scene;
    }

    public static GameRegistry runScene(Scene2 scene) {
        lastInstance.switchScene(scene);
        return lastInstance;
    }

    public static GameRegistry getInstance() {
        return lastInstance;
    }

}
