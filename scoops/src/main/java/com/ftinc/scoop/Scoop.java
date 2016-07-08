package com.ftinc.scoop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.binding.IBinding;
import com.ftinc.scoop.binding.StatusBarBinding;
import com.ftinc.scoop.binding.ViewBinding;
import com.ftinc.scoop.internal.ToppingBinder;
import com.ftinc.scoop.util.AttrUtils;
import com.ftinc.scoop.util.BindingUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ftinc.scoop.SugarCone.BINDERS;
import static com.ftinc.scoop.SugarCone.NOP_VIEW_BINDER;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop
 * Created by drew.heavner on 6/7/16.
 */

public class Scoop {
    private static final String TAG = "Scoop";

    /***********************************************************************************************
     *
     * Singleton Configuration
     *
     */

    private static Scoop _instance = null;

    // TODO: Find a better name for this
    public static Scoop getInstance(){
        if(_instance == null) _instance = new Scoop();
        return _instance;
    }

    /**
     * Create a builder instance for this class to initialize the library
     *
     * @return      the Builder to initialize the library with
     */
    public static Builder waffleCone(){
        return new Builder();
    }

    /**
     * @deprecated Please just use the {@link #getInstance()} method of Scoop to access
     * the bind methods
     */
    @Deprecated
    public static SugarCone sugarCone(){
        Scoop instance = getInstance();
        instance.checkInit();
        return instance.mSugarCone;
    }

    /***********************************************************************************************
     *
     * Constants
     *
     */

    static final String PREFERENCE_FLAVOR_KEY = "com.ftinc.scoop.preference.FLAVOR_KEY";
    static final String PREFERENCE_DAYNIGHT_KEY = "com.ftinc.scoop.preference.DAY_NIGHT_KEY";

    /***********************************************************************************************
     *
     * Variables
     *
     */

    /**
     * Static mapping of all the available base application themes to use/apply
     * mapped by a developer defined ID
     */
    private List<Flavor> mFlavors = new ArrayList<>();

    /**
     * Mapping of all the toppings that the user has binded for
     */
    private SparseArray<Topping> mToppings = new SparseArray<>();

    /**
     * Mapping of all the bindings per class
     */
    private HashMap<Class, Set<IBinding>> mBindings = new HashMap<>();

    /**
     * The index of the default flavor value
     */
    private int mDefaultFlavorIndex = 0;

    /**
     * Determine if initialized
     */
    private boolean mInitialized = false;

    /**
     * SharedPreference store to handle and save changes to the base theme configuration
     */
    private SharedPreferences mPreferences;

    /**
     * SugarCone instance to track the deeper color customizations
     */
    private SugarCone mSugarCone;

    /**
     * Debug flag for logging
     */
    private static boolean debug = false;

    /**
     * Private constructor to prevent initialization
     */
    private Scoop(){}

    /***********************************************************************************************
     *
     * Private Helper Methods
     *
     */

    /**
     * Initialize this helper class with the provided builder
     * @param builder
     */
    private void initialize(Builder builder){
        // Validate builder
        if(builder.prefs != null && !builder.flavors.isEmpty()){

            // Set Preference Storage
            mPreferences = builder.prefs;

            // Set Flavors
            mFlavors = new ArrayList<>(builder.flavors);

            // Set the default flavor if configured
            if(builder.defaultFlavor != null){
                mDefaultFlavorIndex = mFlavors.indexOf(builder.defaultFlavor);
            }

            // Deprecated
            mSugarCone = new SugarCone();

            // Set init flag
            mInitialized = true;

        }else {
            throw new IllegalStateException("SharedPreferences and at least one flavor must be set");
        }
    }

    /**
     * Get the index of the current configured flavor
     *
     * @return      the index of the current flavor to apply
     */
    private int getCurrentFlavorIndex(){
        checkInit();

        // Get the selected flavor index from the preference storage
        int flavorIndex = mPreferences.getInt(PREFERENCE_FLAVOR_KEY, mDefaultFlavorIndex);

        // Verify that index is valid
        if(flavorIndex > -1 && flavorIndex < mFlavors.size()){
            return flavorIndex;
        }

        return mDefaultFlavorIndex;
    }

    /**
     * Get the current selected scoop of flavor
     *
     * @param excludeDefault        whether or not to return null if the current selected is the default theme
     * @return                      the current scoop of flavor
     */
    private Flavor getCurrentFlavor(boolean excludeDefault){
        int index = getCurrentFlavorIndex();
        if(index != mDefaultFlavorIndex || !excludeDefault) {
            return mFlavors.get(index);
        }
        return null;
    }

    /**
     * Verify the initialization state of the utility
     */
    private void checkInit(){
        if(!mInitialized) throw new IllegalStateException("Scoop needs to be initialized first!");
    }

    @NonNull @UiThread
    private ToppingBinder<Object> getViewBinder(@NonNull Object target) {
        Class<?> targetClass = target.getClass();
        if (debug) Log.d(TAG, "Looking up topping binder for " + targetClass.getName());
        return findViewBinderForClass(targetClass);
    }

    @NonNull @UiThread
    private ToppingBinder<Object> findViewBinderForClass(Class<?> cls) {
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

    /**
     * Get the set of bindings for a given class
     *
     * @param clazz     the class key for the bindings to look up
     * @return          the set of bindings for the class
     */
    private Set<IBinding> getBindings(Class clazz){
        Set<IBinding> bindings = mBindings.get(clazz);
        if(bindings == null){
            bindings = new HashSet<>();
            mBindings.put(clazz, bindings);
        }
        return bindings;
    }

    /**
     * Find the {@link Topping} object for it's given Id or create one if not found
     *
     * @param toppingId         the id of the topping to get
     * @return                  the topping associated with the id
     */
    private Topping getOrCreateTopping(int toppingId){
        Topping topping = mToppings.get(toppingId);
        if(topping == null){
            topping = new Topping(toppingId);
            mToppings.put(toppingId, topping);
        }

        return topping;
    }

    /***********************************************************************************************
     *
     * Public Methods
     *
     */

    /**
     * Enable debug logging
     */
    public static void setDebug(boolean flag){
        debug = flag;
    }

    /**
     * Get the selected day night mode to use with certain themes
     *
     * @return      the day night mode to use
     */
    @AppCompatDelegate.NightMode
    public int getDayNightMode(){
        checkInit();
        return mPreferences.getInt(PREFERENCE_DAYNIGHT_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    /**
     * Get the list of available flavors that you can scoop from
     *
     * @return
     */
    public List<Flavor> getFlavors(){
        return Collections.unmodifiableList(mFlavors);
    }

    /**
     * Get the current flavor to apply
     *
     * @return      one scoop of ice cream
     */
    public Flavor getCurrentFlavor(){
        return getCurrentFlavor(false);
    }

    /**
     * Apply the current {@link Flavor} to the given activity based on the user's selected preference.
     *
     * @param activity      the activity to apply the selected theme configuration to
     */
    @SuppressWarnings("WrongConstant")
    public void apply(Activity activity){
        Flavor flavor = getCurrentFlavor(true);
        if(flavor != null){
            // Apply DayNight mode setting if applicable
            if(flavor.isDayNight()){
                AppCompatDelegate.setDefaultNightMode(getDayNightMode());
            }

            // Apply theme
            apply(activity, flavor.getStyleResource());
        }
    }

    /**
     * Apply the current {@link Flavor}s Dialog theme to the activity to give it a Dialog like
     * appearance based on the user selected preference
     *
     * @param activity      the activity to apply the dialog theme to
     */
    @SuppressWarnings("WrongConstant")
    public void applyDialog(Activity activity){
        Flavor flavor = getCurrentFlavor(true);
        if(flavor != null && flavor.getDialogStyleResource() > -1){
            // Apply DayNight mode setting if applicable
            if(flavor.isDayNight()){
                AppCompatDelegate.setDefaultNightMode(getDayNightMode());
            }

            // Apply theme
            apply(activity, flavor.getDialogStyleResource());
        }
    }

    /**
     * Apply the desired theme to an activity and it's window
     *
     * @param activity      the activity to apply to
     * @param theme         the theme to apply
     */
    private void apply(Activity activity, @StyleRes int theme){
        // Apply theme
        activity.setTheme(theme);

        // Ensure window background get's properly set
        int color = AttrUtils.getColorAttr(activity, android.R.attr.colorBackground);
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(color));
    }

    /**
     * Apply the attributed menu item tint to all the icons if the attribute {@link R.attr#toolbarItemTint}
     *
     * @param context      the application context to derive the attr color from
     * @param menu          the menu to apply to
     */
    public void apply(Context context, Menu menu){
        Flavor flavor = getCurrentFlavor();
        if(menu != null && menu.size() > 0 && flavor != null){
            int tint = AttrUtils.getColorAttr(context, flavor.getStyleResource(), R.attr.toolbarItemTint);
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                Drawable icon = item.getIcon();
                if(icon != null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        icon.setTint(tint);
                    }else{
                        icon.setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        }
    }

    /**
     * Choose a given flavor
     *
     * @param item      the flavor to scoop
     */
    public void choose(Flavor item) {
        checkInit();
        int index = mFlavors.indexOf(item);
        mPreferences.edit().putInt(PREFERENCE_FLAVOR_KEY, index).apply();
    }

    /**
     * Choose the DayNight mode you want to use for selected day/night mode themes
     *
     * @param mode      the daynight mode you wish to use
     */
    public void chooseDayNightMode(@AppCompatDelegate.NightMode int mode){
        checkInit();
        mPreferences.edit().putInt(PREFERENCE_DAYNIGHT_KEY, mode).apply();
    }

    /***********************************************************************************************
     *
     * Topping Binding Methods
     *
     */

    /**
     * Bind all the annotated elements to a given activity
     *
     * @see BindTopping
     * @see BindToppingStatus
     *
     * @param activity      the activity to bind to
     */
    public void bind(Activity activity){
        List<IBinding> bindings = getViewBinder(activity).bind(activity);

        // add to system
        Set<IBinding> _bindings = getBindings(activity.getClass());
        _bindings.addAll(bindings);
    }

    /**
     * Bind a view to a topping on a given object
     *
     * @param obj               the class the view belongs to
     * @param toppingId         the id of the topping to bind to
     * @param view              the view to bind
     * @return                  self for chaining
     */
    public Scoop bind(Object obj, int toppingId, View view){
        return bind(obj, toppingId, view, null);
    }

    /**
     * Bind a view to a topping on a given object with a specified color adapter
     *
     * @param obj               the classs the view belongs to
     * @param toppingId         the id of the topping
     * @param view              the view to bind
     * @param colorAdapter      the color adapter to bind with
     * @return                  self for chaining
     */
    public Scoop bind(Object obj, int toppingId, View view, @Nullable ColorAdapter colorAdapter){
        return bind(obj, toppingId, view, colorAdapter, null);
    }

    /**
     * Bind a view to a topping on a given object with a specified color adapter and change animation
     * interpolator
     *
     * @param obj               the class the view belongs to
     * @param toppingId         the id of the topping
     * @param view              the view to bind
     * @param colorAdapter      the color adapter to bind with
     * @param interpolator      the interpolator to use when switching colors
     * @return                  self for chaining
     */
    public Scoop bind(Object obj, int toppingId, View view, @Nullable ColorAdapter colorAdapter, @Nullable Interpolator interpolator){

        // Get a default color adapter if not supplied
        if(colorAdapter == null){
            colorAdapter = BindingUtils.getColorAdapter(view.getClass());
        }

        // Generate Binding
        IBinding binding = new ViewBinding(toppingId, view, colorAdapter, interpolator);

        // Bind
        return bind(obj, toppingId, binding);
    }

    /**
     * Bind the status bar of an activity to a topping so that it's color is updated when the
     * user/developer updates the color for that topping id
     *
     * @param activity      the activity whoes status bar to bind to
     * @param toppingId     the id of the topping to bind with
     * @return              self for chaining
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Scoop bindStatusBar(Activity activity, int toppingId){
        return bindStatusBar(activity, toppingId, null);
    }

    /**
     * Bind the status bar of an activity to a topping so that it's color is updated when the
     * user/developer updates the color for that topping id and animation it's color change using
     * the provided interpolator
     *
     * @param activity      the activity whoes status bar to bind to
     * @param toppingId     the id of the topping to bind with
     * @param interpolator  the interpolator that defines how the animation for the color change will run
     * @return              self for chaining
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Scoop bindStatusBar(Activity activity, int toppingId, @Nullable Interpolator interpolator){
        IBinding binding = new StatusBarBinding(toppingId, activity, interpolator);
        return bind(activity, toppingId, binding);
    }

    /**
     * Provide a custom binding to a certain topping id on a given object. This allows you to
     * customize the changes between color on certain properties, i.e. Toppings, to define it
     * to your use case
     *
     * @param obj               the object to bind on
     * @param toppingId         the topping id to bind to
     * @param binding           the binding that defines how your custom properties are updated
     * @return                  self for chaining
     */
    public Scoop bind(Object obj, int toppingId, IBinding binding){

        // Find or Create Topping
        Topping topping = getOrCreateTopping(toppingId);

        // Store binding
        Set<IBinding> bindings = getBindings(obj.getClass());
        bindings.add(binding);

        return this;
    }

    /**
     * Unbind all bindings on a certain class
     *
     * @param obj       the class/object that you previously made bindings to (i.e. an Activity, or Fragment)
     */
    public void unbind(Object obj){
        Set<IBinding> bindings = getBindings(obj.getClass());
        for (IBinding binding : bindings) {
            binding.unbind();
        }

        // Clear the bindings out of the map
        mBindings.remove(obj.getClass());
    }

    /**
     * Update a topping, i.e. color property, with a new color and therefore sending it out to
     * all your bindings
     *
     * @param toppingId     the id of the topping you wish to update
     * @param color         the updated color to update to
     * @return              self for chaining.
     */
    public Scoop update(int toppingId, @ColorInt int color){

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
            throw new InvalidParameterException("Nothing has been bound to the Topping of the given id (" + toppingId + ").");
        }

        return this;
    }

    /***********************************************************************************************
     *
     * Initialization Builder
     *
     */

    public static class Builder{

        private SharedPreferences prefs;
        private Flavor defaultFlavor;
        private final List<Flavor> flavors;

        Builder(){
            flavors = new ArrayList<>();
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId){
            return addFlavor(name, styleResourceId, -1, false);
        }

        public Builder addDayNightFlavor(String name,
                                 @StyleRes int styleResourceId){
            return addFlavor(name, styleResourceId, -1, false, true);
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId,
                                 boolean isDefault){
            return addFlavor(name, styleResourceId, -1, isDefault);
        }

        public Builder addDayNightFlavor(String name,
                                 @StyleRes int styleResourceId,
                                 boolean isDefault){
            return addFlavor(name, styleResourceId, -1, isDefault, true);
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId,
                                 @StyleRes int dialogStyleResourceId){
            return addFlavor(name, styleResourceId, dialogStyleResourceId, false);
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId,
                                 @StyleRes int dialogStyleResourceId,
                                 boolean isDefault){
            return addFlavor(name, styleResourceId, dialogStyleResourceId, isDefault, false);
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId,
                                 @StyleRes int dialogStyleResourceId,
                                 boolean isDefault,
                                 boolean isDayNight){
            Flavor flavor = new Flavor(name, styleResourceId, dialogStyleResourceId, isDayNight);
            if(isDefault) defaultFlavor = flavor;
            return addFlavor(flavor);
        }

        public Builder addFlavor(Flavor... flavor){
            flavors.addAll(Arrays.asList(flavor));
            return this;
        }

        /**
         * @deprecated      Toppings no longer need to be pre-instantiated
         */
        @Deprecated
        public Builder addToppings(Topping... toppings){
            // This does nothing now
            return this;
        }

        public Builder setSharedPreferences(SharedPreferences prefs){
            this.prefs = prefs;
            return this;
        }

        public void initialize(){
            Scoop.getInstance()
                    .initialize(this);
        }

    }



}
