package com.ftinc.themeenginetest.adapters;

import android.support.annotation.ColorInt;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.Utils;

/**
 * Created by r0adkll on 6/26/16.
 */

public class ProgressBarColorAdapter implements ColorAdapter<ProgressBar> {
    @Override
    public void applyColor(ProgressBar view, @ColorInt int color) {
        view.setIndeterminateTintList(Utils.colorToStateList(color));
    }

    @Override
    public int getColor(ProgressBar view) {
        if(view.getIndeterminateTintList() != null){
            return view.getIndeterminateTintList().getDefaultColor();
        }
        return 0;
    }
}
