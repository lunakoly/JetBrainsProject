package ru.luna_koly.jetbrainsproject.basic_shapes;

import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.util.VertexFormatter;

/**
 * Created with love by luna_koly on 18.06.17.
 */

public class VertexRectangle extends AbstractVertexShape {
    private static final short[] drawOrder = {0, 1, 2, 2, 3, 0};


    public VertexRectangle(float[] vertices) {
        if (vertices.length != 12)
            this.vertices = VertexFormatter.generateEmptyVertexArray(9);
        else
            this.vertices = vertices;

        this.vertices = VertexFormatter.getTriadOfPattern(vertices, drawOrder);
        genBuffer();

        // set to default
        shaderProgram = GameRenderer.getDefaultShaderProgram().getCurrentProgram();
    }
}
