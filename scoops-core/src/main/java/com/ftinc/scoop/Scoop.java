package com.ftinc.scoop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;

import com.ftinc.scoop.model.Flavor;
import com.ftinc.scoop.model.SugarCone;
import com.ftinc.scoop.model.Topping;
import com.ftinc.scoop.util.AttrUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop
 * Created by drew.heavner on 6/7/16.
 */

public class Scoop {

    /***********************************************************************************************
     *
     * Singleton Configuration
     *
     */

    private static Scoop _instance = null;

    public static Scoop getInstance(){
        if(_instance == null) _instance = new Scoop();
        return _instance;
    }

    public static Builder waffleCone(){
        return new Builder();
    }

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

            // SugarCone
            mSugarCone = new SugarCone();
            mSugarCone.addToppings(builder.toppings);

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

    /***********************************************************************************************
     *
     * Public Methods
     *
     */

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
     * Initialization Builder
     *
     */

    public static class Builder{

        private SharedPreferences prefs;
        private Flavor defaultFlavor;
        private final List<Flavor> flavors;
        private final List<Topping> toppings;

        Builder(){
            flavors = new ArrayList<>();
            toppings = new ArrayList<>();
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

        public Builder addToppings(Topping... toppings){
            this.toppings.addAll(Arrays.asList(toppings));
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
