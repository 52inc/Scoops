package com.ftinc.scoop.adapters;

import android.support.annotation.ColorInt;
import android.view.View;

/**
 * An adapter that dictates how a color property or change is applied
 * to a given view
 */
public interface ColorAdapter<T extends View> {

    /**
     * Apply the color to the given view
     *
     * @param view      the view to apply the color to
     * @param color     the color to apply
     */
    void applyColor(T view, @ColorInt int color);

    /**
     * Get the current color for the element
     *
     * @param view      the view to get the color from
     * @return          the current color
     */
    @ColorInt
    int getColor(T view);

}
