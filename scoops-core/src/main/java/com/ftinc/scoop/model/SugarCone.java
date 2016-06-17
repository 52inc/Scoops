package com.ftinc.scoop.model;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static final long DEFAULT_ANIMATION_DURATION = 300L;

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private List<Topping> mToppings = new ArrayList<>();

    /***********************************************************************************************
     *
     * Api Methods
     *
     */

    public void update(final View view, Interpolator interpolator, Topping property){
        final ColorAdapter adapter = getColorAdapter(view.getClass());

        int fromColor = property.getPreviousColor();
        int toColor = property.getColor();

        ValueAnimator anim;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            anim = ValueAnimator.ofArgb(fromColor, toColor)
                    .setDuration(DEFAULT_ANIMATION_DURATION);
        }else{
            anim = ValueAnimator.ofInt(fromColor, toColor)
                    .setDuration(DEFAULT_ANIMATION_DURATION);
            anim.setEvaluator(new ArgbEvaluator());
        }

        anim.setInterpolator(interpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int colorValue = (int) valueAnimator.getAnimatedValue();
                adapter.applyColor(view, colorValue);
            }
        });

    }

    public void bind(Activity activity){
        // Do Some Binding

    }

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    private <T extends View> ColorAdapter<T> getColorAdapter(Class<T> clazz){
        ColorAdapter adapter = COLOR_ADAPTERS.get(clazz);
        if(adapter == null){
            adapter = new DefaultColorAdapter();
        }
        return adapter;
    }


    public <T extends Topping> SugarCone addTopping(T topping) {
        mToppings.add(topping);
        return this;
    }
}
