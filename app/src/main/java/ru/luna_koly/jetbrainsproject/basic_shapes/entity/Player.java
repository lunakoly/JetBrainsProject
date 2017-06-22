package ru.luna_koly.jetbrainsproject.basic_shapes.entity;

import android.content.Context;

import ru.luna_koly.jetbrainsproject.basic_shapes.util.Texture;

/**
 * Created with love by luna_koly on 6/21/17.
 */

public class Player extends Human {
    private static String NAME = "Charly";

    public Player(Context context) {
        super(context, NAME, new Texture(context, "char/charly_standing.png", 1, 1000));
        setSize(0.5f, 1.1f);
        moveY(-0.2f);
    }

}
