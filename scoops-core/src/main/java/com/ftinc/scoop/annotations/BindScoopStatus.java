package com.ftinc.scoop.annotations;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.annotations
 * Created by drew.heavner on 6/24/16.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface BindScoopStatus {
    int value();

    /**
     * OPTIONAL
     * The Interpolator to use when changing color properties on views
     */
    Class<? extends Interpolator> interpolator() default LinearInterpolator.class;
}
