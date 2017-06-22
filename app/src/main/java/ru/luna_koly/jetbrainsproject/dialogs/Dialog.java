package ru.luna_koly.jetbrainsproject.dialogs;

import java.util.HashMap;

/**
 * Created by luna_koly on 6/22/17.
 */

public class Dialog extends HashMap<String, Replica> {
    private boolean isEmpty = true;

    @Override
    public Replica put(String key, Replica value) {
        isEmpty = false;
        return super.put(key, value);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }
}
