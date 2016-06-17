package com.ftinc.scoop.adapters;

import android.support.annotation.ColorInt;
import android.view.View;

/**
 * The default color adapter that just applies the color to the View background
 * 
 */
public class DefaultColorAdapter implements ColorAdapter<View> {
    @Override
    public void applyColor(View view, @ColorInt int color) {
        view.setBackgroundColor(color);
    }
}
