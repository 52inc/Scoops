package com.ftinc.scoop.adapters;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by r0adkll on 6/17/16.
 */

public class ViewGroupColorAdapter implements ColorAdapter<ViewGroup> {
    @Override
    public void applyColor(ViewGroup view, @ColorInt int color) {
        view.setBackgroundColor(color);
    }

    @Override
    public int getColor(ViewGroup view) {
        Drawable bg = view.getBackground();
        if(bg instanceof ColorDrawable){
            return ((ColorDrawable) bg).getColor();
        }
        return 0;
    }
}
