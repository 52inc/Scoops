package com.ftinc.scoop.adapters;

import android.support.annotation.ColorInt;
import android.view.View;

/**
 * Created by r0adkll on 6/16/16.
 */

public interface ColorAdapter<T extends View> {

    /**
     * Apply the color to the given view
     *
     * @param view
     * @param color
     */
    void applyColor(T view, @ColorInt int color);

}
