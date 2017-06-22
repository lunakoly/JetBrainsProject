package ru.luna_koly.jetbrainsproject.util;

import java.util.HashMap;

/**
 * Created by luna_koly on 6/22/17.
 */

public class ResourceHolder<T> extends HashMap<String, T> {
    private boolean isEmpty = true;

    @Override
    public T put(String key, T value) {
        isEmpty = false;
        return super.put(key, value);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

}
