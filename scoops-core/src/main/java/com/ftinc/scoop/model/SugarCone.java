package com.ftinc.scoop.model;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
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
import com.ftinc.scoop.annotations.BindScoop;
import com.ftinc.scoop.annotations.BindScoopStatus;
import com.ftinc.scoop.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private static final String TAG = "SugarCone";

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
    private HashMap<Class, Set<IBinding>> mBindings = new HashMap<>();

    /***********************************************************************************************
     *
     * Api Methods
     *
     */

    public void bind(Activity activity){
        // Check activity for BindScoopStatus
        if(activity.getClass().isAnnotationPresent(BindScoopStatus.class)){
            BindScoopStatus annotation = activity.getClass().getAnnotation(BindScoopStatus.class);
            int toppingId = annotation.value();
            Interpolator interpolator = null;
            try {
                interpolator = annotation.interpolator().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            Log.d(TAG, String.format("StatusBar Binding(%d, %s)", toppingId, interpolator));

            // Create status bar binding
            bindStatusBar(activity, toppingId, interpolator);
        }

        // Scan Fields
        for (Field field : activity.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(BindScoop.class)) {
                BindScoop annotation = field.getAnnotation(BindScoop.class);
                Class type = field.getType();
                if(ReflectionUtils.isTypeOf(type, View.class)) {
                    field.setAccessible(true);

                    // Build Binding
                    try {
                        View view = (View) field.get(activity);

                        // Get Color Adapter
                        ColorAdapter adapter = annotation.adapter().newInstance();
                        if(adapter instanceof DefaultColorAdapter){
                            adapter = getColorAdapter(type);
                        }

                        // Get Interpolator
                        Interpolator interpolator = annotation.interpolator().newInstance();

                        // Get the property topping id
                        int toppingId = annotation.value();

                        Log.d(TAG, String.format("View Binding [%d](%s, %s, %s)", toppingId, view, adapter, interpolator));

                        // Create Binding
                        bind(activity, toppingId, view, adapter, interpolator);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public SugarCone bind(Object obj, int toppingId, View view){
        return bind(obj, toppingId, view, null);
    }

    public SugarCone bind(Object obj, int toppingId, View view, @Nullable ColorAdapter colorAdapter){
        return bind(obj, toppingId, view, colorAdapter, null);
    }

    public SugarCone bind(Object obj, int toppingId, View view, @Nullable ColorAdapter colorAdapter, @Nullable Interpolator interpolator){

        // Get a default color adapter if not supplied
        if(colorAdapter == null){
            colorAdapter = getColorAdapter(view.getClass());
        }

        // Generate Binding
        IBinding binding = new ViewBinding(toppingId, view, colorAdapter, interpolator);

        // Bind
        return bind(obj, toppingId, binding);
    }

    public SugarCone bindStatusBar(Activity activity, int toppingId){
        return bindStatusBar(activity, toppingId, null);
    }

    public SugarCone bindStatusBar(Activity activity, int toppingId, @Nullable Interpolator interpolator){
        IBinding binding = new StatusBarBinding(toppingId, activity, interpolator);
        return bind(activity, toppingId, binding);
    }

    public SugarCone bind(Object obj, int toppingId, IBinding binding){

        // Find Topping
        Topping topping = mToppings.get(toppingId);
        if(topping != null){

            // Store binding
            Set<IBinding> bindings = getBindings(obj.getClass());
            bindings.add(binding);

            return this;
        }else{
            throw new InvalidParameterException("No Topping for the given id (" + toppingId + ") was found.");
        }

    }

    public void unbind(Object obj){
        Set<IBinding> bindings = getBindings(obj.getClass());
        for (IBinding binding : bindings) {
            binding.unbind();
        }

        // Clear the bindings out of the map
        mBindings.remove(obj.getClass());
    }

    public SugarCone update(int toppingId, @ColorInt int color){

        // Get the topping
        Topping topping = mToppings.get(toppingId);
        if(topping != null){
            topping.updateColor(color);

            // Update bindings
            Collection<Set<IBinding>> bindings = mBindings.values();
            for (Set<IBinding> bindingSet : bindings) {
                for (IBinding binding : bindingSet) {
                    if(binding.getToppingId() == toppingId) {
                        binding.update(topping);
                    }
                }
            }

        }else{
            throw new InvalidParameterException("No Topping for the given id (" + toppingId + ") was found.");
        }

        return this;
    }

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */


    Set<IBinding> getBindings(Class clazz){
        Set<IBinding> bindings = mBindings.get(clazz);
        if(bindings == null){
            bindings = new HashSet<>();
            mBindings.put(clazz, bindings);
        }
        return bindings;
    }

    <T extends View> ColorAdapter<T> getColorAdapter(Class<T> clazz){
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

    public SparseArray<Topping> getToppings(){
        return mToppings;
    }
}
