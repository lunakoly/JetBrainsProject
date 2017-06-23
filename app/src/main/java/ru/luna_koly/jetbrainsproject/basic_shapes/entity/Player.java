package ru.luna_koly.jetbrainsproject.basic_shapes.entity;

import android.content.Context;

import ru.luna_koly.jetbrainsproject.GameRenderer;

/**
 * Created with love by luna_koly on 6/21/17.
 */

public class Player extends Human {
    private static String NAME = "Charly";

    public Player(Context context) {
        super(context, NAME, GameRenderer.getPlayerStanding());
        setSize(0.5f, 1.1f);
        moveY(-0.2f);
    }

}
