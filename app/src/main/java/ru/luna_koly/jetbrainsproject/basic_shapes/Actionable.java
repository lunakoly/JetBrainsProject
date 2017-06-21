package ru.luna_koly.jetbrainsproject.basic_shapes;

/**
 * Created with love by luna_koly on 21.06.17.
 */

public interface Actionable {
    void activate();
    void addActivationResult(Actionable result);
}
