package com.ftinc.themeenginetest.adapters;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.widget.Button;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.Utils;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.themeenginetest.adapters
 * Created by drew.heavner on 6/24/16.
 */

public class ButtonColorAdapter implements ColorAdapter<Button> {
    @Override
    public void applyColor(Button view, @ColorInt int color) {
        ColorStateList colorStateList = Utils.colorToStateList(color);
        view.setBackgroundTintList(colorStateList);
    }

    @Override
    public int getColor(Button view) {
        if(view.getBackgroundTintList() != null) {
            return view.getBackgroundTintList().getDefaultColor();
        }
        return 0;
    }
}
