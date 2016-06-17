package com.ftinc.scoop.adapters;

import android.os.Build;
import android.support.annotation.ColorInt;
import android.widget.CheckBox;
import android.widget.Switch;

import com.ftinc.scoop.util.Utils;

/**
 * Created by r0adkll on 6/17/16.
 */

public class CheckBoxColorAdapter implements ColorAdapter<CheckBox> {
    @Override
    public void applyColor(CheckBox view, @ColorInt int color) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setButtonTintList(Utils.colorToStateList(color));
        }
    }
}
