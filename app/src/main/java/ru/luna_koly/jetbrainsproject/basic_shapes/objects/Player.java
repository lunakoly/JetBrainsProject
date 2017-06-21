package ru.luna_koly.jetbrainsproject.basic_shapes.objects;

import android.content.Context;

import ru.luna_koly.jetbrainsproject.GameRenderer;

/**
 * Created by user on 6/21/17.
 */

public class Player extends Human {
    public static String NAME = "Charly";

    public Player(Context context) {
        super(context, NAME, "char/charly_standing.png");
        setSize(0.5f, 1.1f);
        moveY(-0.2f);
    }

}
