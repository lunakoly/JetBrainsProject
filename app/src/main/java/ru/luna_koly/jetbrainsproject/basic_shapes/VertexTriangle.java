package ru.luna_koly.jetbrainsproject.basic_shapes;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.util.VertexFormatter;

/**
 * Created with love by luna_koly on 18.06.17.
 */

public class VertexTriangle extends AbstractVertexShape {
    public VertexTriangle(float[] vertices) {
        if (vertices.length != 9)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        genBuffer();

        // set to default
        shaderProgram = GameRenderer.getDefaultShaderProgram().getCurrentProgram();
    }
}
