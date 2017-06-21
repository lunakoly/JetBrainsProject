package ru.luna_koly.jetbrainsproject;

import java.util.ArrayList;
import java.util.HashMap;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;

/**
 * Created with love by luna_koly on 20.06.17.
 */

public class GameRegistry {
    private static GameRegistry lastInstance;

    private Engine engine;
    private HashMap<String, Scene> scenes = new HashMap<>();
    private ArrayList<Runnable> startupAlgorithms = new ArrayList<>();


    GameRegistry(Engine engine) {
        this.engine = engine;
        lastInstance = this;
    }

    public static GameRegistry addScene(Scene scene, String key) {
        lastInstance.scenes.put(key, scene);
        return lastInstance;
    }

    public static Scene getScene(String key) {
        return lastInstance.scenes.get(key);
    }

    public static GameRegistry runScene(Scene scene) {
        lastInstance.engine.getRenderer().setTargetScene(scene);
        return lastInstance;
    }

    static GameRegistry addStartupAlgorithm(Runnable algorithm) {
        lastInstance.startupAlgorithms.add(algorithm);
        return lastInstance;
    }

    static GameRegistry runStartupAlgorithms() {
        for (Runnable r : lastInstance.startupAlgorithms)
            r.run();

        return lastInstance;
    }

    public static GameSurface getSurface() {
        return lastInstance.engine.getSurface();
    }
}
