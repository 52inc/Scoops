package com.ftinc.themeenginetest.adapters;

import android.support.annotation.ColorInt;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.util.AttrUtils;
import com.ftinc.scoop.util.Utils;
import com.ftinc.themeenginetest.R;

/**
 * Created by r0adkll on 6/26/16.
 */

public class CompoundButtonColorAdapter implements ColorAdapter<CompoundButton>{

    @Override
    public void applyColor(CompoundButton view, @ColorInt int color) {
        int disabledColor = AttrUtils.getColorAttr(view.getContext(), R.attr.colorControlNormal);
        view.setButtonTintList(Utils.colorToStateList(color, disabledColor));
    }

    @Override
    public int getColor(CompoundButton view) {
        if(view.getButtonTintList() != null){
            return view.getButtonTintList().getDefaultColor();
        }
        return 0;
    }
}
