package com.ftinc.scoop.model;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.ftinc.scoop.adapters.ColorAdapter;

/**
 * Created by r0adkll on 6/17/16.
 */

public class Binding {

    private static final long DEFAULT_ANIMATION_DURATION = 300L;

    private View mView;
    private Interpolator mInterpolator;
    private ColorAdapter mColorAdapter;
    private int mToppingId;

    private ValueAnimator mAnimator;

    public Binding(int toppingId,
                   @NonNull View view,
                   @NonNull ColorAdapter adapter,
                   @Nullable Interpolator interpolator){
        mToppingId = toppingId;
        mView = view;
        mColorAdapter = adapter;
        mInterpolator = interpolator;
    }

    public int getToppingId(){
        return mToppingId;
    }

    public void update(Topping topping){

        int fromColor = topping.getPreviousColor() != 0 ?
                topping.getPreviousColor() : mColorAdapter.getColor(mView) != 0 ?
                        mColorAdapter.getColor(mView) : Color.WHITE;
        int toColor = topping.getColor();

        if(mAnimator != null){
            mAnimator.cancel();
            mAnimator = null;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAnimator = ValueAnimator.ofArgb(fromColor, toColor)
                    .setDuration(DEFAULT_ANIMATION_DURATION);
        }else{
            mAnimator = ValueAnimator.ofInt(fromColor, toColor)
                    .setDuration(DEFAULT_ANIMATION_DURATION);
            mAnimator.setEvaluator(new ArgbEvaluator());
        }

        mAnimator.setInterpolator(mInterpolator);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int colorValue = (int) valueAnimator.getAnimatedValue();
                mColorAdapter.applyColor(mView, colorValue);
            }
        });

    }

    public void unbind() {
        mView = null;
        mInterpolator = null;
    }
}
