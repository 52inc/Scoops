package com.ftinc.scoop.model;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.adapters.DefaultColorAdapter;
import com.ftinc.scoop.adapters.ImageViewColorAdapter;
import com.ftinc.scoop.adapters.TextViewColorAdapter;
import com.ftinc.scoop.adapters.ViewGroupColorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private SparseArray<Topping> mToppings = new SparseArray<>();
    private List<Binding> mBindings = new ArrayList<>();

    /***********************************************************************************************
     *
     * Api Methods
     *
     */

    public Binding bind(int toppingId, View view){
        return bind(toppingId, view, null);
    }

    /**
     * Bind a view to a specific topping
     *
     * @param toppingId         the topping to bind the view to changes to
     * @param view              the view to update based on property changes
     * @param interpolator      the interpolator to use to animate the changes
     * @return                  the Binding to use
     */
    public Binding bind(int toppingId, View view, Interpolator interpolator){
        Topping topping = mToppings.get(toppingId);
        if(topping != null){
            Binding binding = new Binding(toppingId, view, getColorAdapter(view.getClass()), interpolator);
            mBindings.add(binding);
            return binding;
        }

        return null;
    }

    public void unbind(int toppingId){
        List<Binding> _trash = new ArrayList<>(mBindings.size());
        for (Binding mBinding : mBindings) {
            if(mBinding.getToppingId() == toppingId){
                mBinding.unbind();
                _trash.add(mBinding);
            }
        }

        mBindings.removeAll(_trash);
    }

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    private <T extends View> ColorAdapter<T> getColorAdapter(Class<T> clazz){
        ColorAdapter adapter = COLOR_ADAPTERS.get(clazz);
        if(adapter == null){

            // Try super class
            adapter = COLOR_ADAPTERS.get(clazz.getSuperclass());
            if(adapter == null) {
                adapter = new DefaultColorAdapter();
            }
        }
        return adapter;
    }


    public SugarCone addTopping(Topping... topping) {
        return addToppings(Arrays.asList(topping));
    }

    public SugarCone addToppings(Collection<Topping> toppings){
        for (Topping topping : toppings) {
            mToppings.put(topping.getId(), topping);
        }
        return this;
    }
}
