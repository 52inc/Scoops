package com.ftinc.scoop.model;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.adapters.DefaultColorAdapter;
import com.ftinc.scoop.adapters.ImageViewColorAdapter;
import com.ftinc.scoop.adapters.TextViewColorAdapter;
import com.ftinc.scoop.adapters.ViewGroupColorAdapter;
import com.ftinc.scoop.model.Topping;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * This class represents the management of individual coloring attributes as to update element colors
 * without having to rebuild the entire activity.
 *
 * I.E. Say if you want to interpolate the colors based on an image loading and then using palette
 *
 * Package: com.ftinc.scoop
 * Created by drew.heavner on 6/17/16.
 */
public class SugarCone {

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final Map<Class, ColorAdapter> COLOR_ADAPTERS = new HashMap<Class, ColorAdapter>(){
        {
            put(View.class, new DefaultColorAdapter());
            put(ViewGroup.class, new ViewGroupColorAdapter());
            put(TextView.class, new TextViewColorAdapter());
            put(ImageView.class, new ImageViewColorAdapter());
        }
    };

    /***********************************************************************************************
     *
     * Variables
     *
     */

    View view;
    Interpolator mInterpolater;
    Topping property;


    void add(View view, Interpolator interpolator, Topping property){

    }

    void update(View view, Interpolator interpolator, Topping property){
        ColorAdapter adapter = COLOR_ADAPTERS.get(view.getClass());

        int fromColor = property.getPreviousColor();
        int toColor = property.getColor();

        ValueAnimator anim = ValueAnimator.ofArgb(fromColor, toColor)
                .setDuration(300);

        anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

            }
        });

    }


}
