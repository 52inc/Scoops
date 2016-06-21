package com.ftinc.scoop.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

    @Override
    public int getColor(View view) {
        Drawable bg = view.getBackground();
        if(bg instanceof ColorDrawable){
            return ((ColorDrawable) bg).getColor();
        }
        return 0;
    }
}
