package com.ftinc.scoop.model;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.animation.Interpolator;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/21/16.
 */

public abstract class AnimatedBinding extends IBinding {

    private ValueAnimator mAnimator;
    private Interpolator mInterpolator;

    public AnimatedBinding(int toppingId, Interpolator interpolator) {
        super(toppingId);
        mInterpolator = interpolator;
    }

    @Override
    void update(Topping topping) {

        int fromColor = topping.getPreviousColor() != 0 ?
                topping.getPreviousColor() : getCurrentColor() != 0 ?
                getCurrentColor() : Color.WHITE;
        int toColor = topping.getColor();

        if (fromColor != toColor) {

            if (mAnimator != null) {
                mAnimator.cancel();
                mAnimator = null;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAnimator = ValueAnimator.ofArgb(fromColor, toColor)
                        .setDuration(DEFAULT_ANIMATION_DURATION);
            } else {
                mAnimator = ValueAnimator.ofInt(fromColor, toColor)
                        .setDuration(DEFAULT_ANIMATION_DURATION);
                mAnimator.setEvaluator(new ArgbEvaluator());
            }

            if (mInterpolator != null) mAnimator.setInterpolator(mInterpolator);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int colorValue = (int) valueAnimator.getAnimatedValue();
                    applyColor(colorValue);
                }
            });

            mAnimator.start();

        } else {
            applyColor(toColor);
        }
    }

    @Override
    void unbind() {
        mInterpolator = null;
        if(mAnimator != null){
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @ColorInt
    abstract int getCurrentColor();

    abstract void applyColor(@ColorInt int color);

}
