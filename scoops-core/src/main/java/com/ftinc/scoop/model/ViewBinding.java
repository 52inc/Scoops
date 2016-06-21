package com.ftinc.scoop.model;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.ftinc.scoop.adapters.ColorAdapter;

/**
 * Created by r0adkll on 6/17/16.
 */

public class ViewBinding extends AnimatedBinding{

    private View mView;
    private ColorAdapter mColorAdapter;

    public ViewBinding(int toppingId,
                       @NonNull View view,
                       @NonNull ColorAdapter adapter,
                       @Nullable Interpolator interpolator){
        super(toppingId, interpolator);
        mView = view;
        mColorAdapter = adapter;
    }

    @Override
    public void unbind() {
        mView = null;
        super.unbind();
    }

    @Override
    int getCurrentColor() {
        return mColorAdapter.getColor(mView);
    }

    @Override
    void applyColor(@ColorInt int color) {
        mColorAdapter.applyColor(mView, color);
    }
}
