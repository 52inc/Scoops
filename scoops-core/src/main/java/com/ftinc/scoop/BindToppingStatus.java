package com.ftinc.scoop;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.annotations
 * Created by drew.heavner on 6/24/16.
 */
@Retention(CLASS)
@Target(TYPE)
public @interface BindToppingStatus {
    int value();

    /**
     * OPTIONAL
     * The Interpolator to use when changing color properties on views
     */
    Class<? extends Interpolator> interpolator() default LinearInterpolator.class;

    /**
     * The duration for the animation, default is 600ms
     */
    long duration() default -1;
}
