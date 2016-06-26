package com.ftinc.themeenginetest.adapters;

import android.os.Build;
import android.support.annotation.ColorInt;
import android.widget.Switch;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.AttrUtils;
import com.ftinc.scoop.util.Utils;
import com.ftinc.themeenginetest.R;

/**
 * Created by r0adkll on 6/26/16.
 */

public class SwitchColorAdapter implements ColorAdapter<Switch> {
    @Override
    public void applyColor(Switch view, @ColorInt int color) {
        int disabledColor = AttrUtils.getColorAttr(view.getContext(), R.attr.colorSwitchThumbNormal);
        int trackDisabledTint = view.getContext().getResources().getColor(R.color.grey_600);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setThumbTintList(Utils.colorToStateList(color, disabledColor));
            view.setTrackTintList(Utils.colorToStateList(color, trackDisabledTint));
        }
    }

    @Override
    public int getColor(Switch view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && view.getThumbTintList() != null) {
            return view.getThumbTintList().getDefaultColor();
        }
        return 0;
    }
}
