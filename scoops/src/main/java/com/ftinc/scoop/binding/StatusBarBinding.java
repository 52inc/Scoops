package com.ftinc.scoop.binding;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.view.animation.Interpolator;

import com.ftinc.scoop.Topping;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/21/16.
 */

public class StatusBarBinding extends AnimatedBinding {

    // Dragons Beware! This will memory leak if not properly unbinded
    private Activity mActivity;

    public StatusBarBinding(int toppingId, Activity activity, Interpolator interpolator) {
        super(toppingId, interpolator);
        mActivity = activity;
    }


    public StatusBarBinding(int toppingId, Activity activity, Interpolator interpolator, long duration) {
        super(toppingId, interpolator, duration);
        mActivity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void update(Topping topping) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.update(topping);
        }
    }

    @Override
    public void unbind() {
        super.unbind();
        mActivity = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    int getCurrentColor() {
        return mActivity.getWindow().getStatusBarColor();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    void applyColor(@ColorInt int color) {
        mActivity.getWindow().setStatusBarColor(color);
    }
}
