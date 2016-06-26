package com.ftinc.themeenginetest.adapters;

import android.support.annotation.ColorInt;
import android.widget.SeekBar;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.Utils;

/**
 * Created by r0adkll on 6/26/16.
 */

public class SeekBarColorAdapter implements ColorAdapter<SeekBar> {
    @Override
    public void applyColor(SeekBar view, @ColorInt int color) {
        view.setIndeterminateTintList(Utils.colorToStateList(color));
        view.setThumbTintList(Utils.colorToStateList(color));
        view.setProgressTintList(Utils.colorToStateList(color));
    }

    @Override
    public int getColor(SeekBar view) {
        if(view.getThumbTintList() != null){
            return view.getThumbTintList().getDefaultColor();
        }
        return 0;
    }
}
