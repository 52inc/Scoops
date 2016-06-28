package com.ftinc.scoop.adapters;

import android.support.annotation.ColorInt;
import android.widget.TextView;

/**
 * Created by r0adkll on 6/17/16.
 */

public class TextViewColorAdapter implements ColorAdapter<TextView> {
    @Override
    public void applyColor(TextView view, @ColorInt int color) {
        view.setTextColor(color);
    }

    @Override
    public int getColor(TextView view) {
        return view.getCurrentTextColor();
    }
}
