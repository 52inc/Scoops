package com.ftinc.themeenginetest.adapters;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.Utils;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.themeenginetest
 * Created by drew.heavner on 6/24/16.
 */

public class FABColorAdapter implements ColorAdapter<FloatingActionButton> {
    @Override
    public void applyColor(FloatingActionButton view, @ColorInt int color) {
        ColorStateList colorStateList = Utils.colorToStateList(color);
        view.setBackgroundTintList(colorStateList);
    }

    @Override
    public int getColor(FloatingActionButton view) {
        return view.getBackgroundTintList().getDefaultColor();
    }
}
