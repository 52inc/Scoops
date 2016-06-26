package com.ftinc.themeenginetest.adapters;

import android.support.annotation.ColorInt;
import android.widget.RatingBar;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.AttrUtils;
import com.ftinc.scoop.util.Utils;
import com.ftinc.themeenginetest.R;

/**
 * Created by r0adkll on 6/26/16.
 */

public class RatingBarColorAdapter implements ColorAdapter<RatingBar> {
    @Override
    public void applyColor(RatingBar view, @ColorInt int color) {
        view.setProgressTintList(Utils.colorToStateList(color));
    }

    @Override
    public int getColor(RatingBar view) {
        if(view.getProgressTintList() != null){
            return view.getProgressTintList().getDefaultColor();
        }
        return 0;
    }
}
