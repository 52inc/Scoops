package com.ftinc.scoop.adapters;

import android.support.annotation.ColorInt;
import android.support.v7.widget.SwitchCompat;

import com.ftinc.scoop.R;
import com.ftinc.scoop.util.AttrUtils;
import com.ftinc.scoop.util.Utils;

/**
 * Created by r0adkll on 6/26/16.
 */

public class SwitchCompatColorAdapter implements ColorAdapter<SwitchCompat> {

    int disabledColor = 0;
    int trackDisabledColor = 0;

    @Override
    public void applyColor(SwitchCompat view, @ColorInt int color) {
        if(disabledColor == 0) disabledColor = AttrUtils.getColorAttr(view.getContext(), R.attr.colorSwitchThumbNormal);
        if(trackDisabledColor == 0) trackDisabledColor = view.getContext().getResources().getColor(R.color.grey_600);
        view.setThumbTintList(Utils.colorToStateList(color, disabledColor));
        view.setTrackTintList(Utils.colorToStateList(color, trackDisabledColor));
    }

    @Override
    public int getColor(SwitchCompat view) {
        if (view.getThumbTintList() != null) {
            return view.getThumbTintList().getDefaultColor();
        }
        return 0;
    }
}
