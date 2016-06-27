package com.ftinc.scoop;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;

import com.ftinc.scoop.binding.IBinding;
import com.ftinc.scoop.binding.StatusBarBinding;
import com.ftinc.scoop.binding.ViewBinding;
import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.internal.ToppingBinder;
import com.ftinc.scoop.model.Topping;
import com.ftinc.scoop.util.BindingUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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

    static final Map<Class<?>, ToppingBinder<Object>> BINDERS = new LinkedHashMap<>();
    static final ToppingBinder<Object> NOP_VIEW_BINDER = new ToppingBinder<Object>() {
        @Override
        public List<IBinding> bind(Object target) {
            return new ArrayList<>();
        }
    };

    private static boolean debug = false;

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

    public void setDebug(boolean flag){
        SugarCone.debug = flag;
    }

    public void bind(Activity activity){
        List<IBinding> bindings = getViewBinder(activity).bind(activity);

        // add to system
        Set<IBinding> _bindings = getBindings(activity.getClass());
        _bindings.addAll(bindings);
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
            colorAdapter = BindingUtils.getColorAdapter(view.getClass());
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

    @NonNull @UiThread
    static ToppingBinder<Object> getViewBinder(@NonNull Object target) {
        Class<?> targetClass = target.getClass();
        if (debug) Log.d(TAG, "Looking up topping binder for " + targetClass.getName());
        return findViewBinderForClass(targetClass);
    }

    @NonNull @UiThread
    private static ToppingBinder<Object> findViewBinderForClass(Class<?> cls) {
        ToppingBinder<Object> viewBinder = BINDERS.get(cls);
        if (viewBinder != null) {
            if (debug) Log.d(TAG, "HIT: Cached in topping binder map.");
            return viewBinder;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            if (debug) Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return NOP_VIEW_BINDER;
        }
        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
            Class<?> viewBindingClass = Class.forName(clsName + "_ToppingBinder");
            //noinspection unchecked
            viewBinder = (ToppingBinder<Object>) viewBindingClass.newInstance();
            if (debug) Log.d(TAG, "HIT: Loaded topping binder class.");
        } catch (ClassNotFoundException e) {
            if (debug) Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
            viewBinder = findViewBinderForClass(cls.getSuperclass());
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to create topping binder for " + clsName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to create topping binder for " + clsName, e);
        }
        BINDERS.put(cls, viewBinder);
        return viewBinder;
    }


    Set<IBinding> getBindings(Class clazz){
        Set<IBinding> bindings = mBindings.get(clazz);
        if(bindings == null){
            bindings = new HashSet<>();
            mBindings.put(clazz, bindings);
        }
        return bindings;
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
