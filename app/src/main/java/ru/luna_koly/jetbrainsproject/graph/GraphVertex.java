package ru.luna_koly.jetbrainsproject.graph;

import ru.luna_koly.jetbrainsproject.util.containers.vec2;
import ru.luna_koly.jetbrainsproject.util.containers.vec3;

/**
 * Created by luna_koly on 6/22/17.
 */

public class GraphVertex {
    public String id = "";
    public vec3 position = new vec3(0, 0, 0);
    public String[] nextVertices = new String[0];
}
