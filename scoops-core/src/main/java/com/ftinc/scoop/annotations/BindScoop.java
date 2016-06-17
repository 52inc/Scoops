package com.ftinc.scoop.annotations;

import android.support.annotation.AttrRes;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.adapters.DefaultColorAdapter;
import com.ftinc.scoop.model.Topping;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.annotations
 * Created by drew.heavner on 6/17/16.
 */
@Retention(CLASS)
@Target(FIELD)
public @interface BindScoop {

    Class<? extends Topping> value();

    /**
     * OPTIONAL
     * The Interpolator to use when changing color properties on views
     */
    Class<? extends Interpolator> interpolator() default LinearInterpolator.class;

    /**
     * OPTIONAL
     * The color adapter that dictates how a color is applied to a view
     */
    Class<? extends ColorAdapter<? extends View>> adapter() default DefaultColorAdapter.class;


}
