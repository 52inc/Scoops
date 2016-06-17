package com.ftinc.scoop.adapters;

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
}
