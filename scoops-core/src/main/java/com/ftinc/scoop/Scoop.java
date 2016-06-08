package com.ftinc.scoop;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StyleRes;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.ftinc.scoop.model.Flavor;
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

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final String PREFERENCE_FLAVOR_KEY = "com.ftinc.scoop.preference.FLAVOR_KEY";

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
        int index = getCurrentFlavorIndex();
        if(index != mDefaultFlavorIndex) {
            return mFlavors.get(index);
        }
        return null;
    }

    /**
     * Apply the current {@link Flavor} to the given activity based on the user's selected preference.
     *
     * @param activity      the activity to apply the selected theme configuration to
     */
    public void apply(Activity activity){
        Flavor flavor = getCurrentFlavor();
        if(flavor != null){

            // Apply theme
            activity.setTheme(flavor.getStyleResource());

            // Ensure window background get's properly set
            int color = AttrUtils.getColorAttr(activity, android.R.attr.colorBackground);
            activity.getWindow().setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    /**
     * Apply the current {@link Flavor}s Dialog theme to the activity to give it a Dialog like
     * appearance based on the user selected preference
     *
     * @param activity      the activity to apply the dialog theme to
     */
    public void applyDialog(Activity activity){
        Flavor flavor = getCurrentFlavor();
        if(flavor != null && flavor.getDialogStyleResource() > -1){

            // Apply theme
            activity.setTheme(flavor.getStyleResource());

            // Ensure window background get's properly set
            int color = AttrUtils.getColorAttr(activity, android.R.attr.colorBackground);
            activity.getWindow().setBackgroundDrawable(new ColorDrawable(color));
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

    /***********************************************************************************************
     *
     * Initialization Builder
     *
     */

    public static class Builder{

        private SharedPreferences prefs;
        private Flavor defaultFlavor;
        private final List<Flavor> flavors;

        public Builder(){
            flavors = new ArrayList<>();
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId){
            return addFlavor(name, styleResourceId, -1, false);
        }

        public Builder addFlavor(String name,
                                 @StyleRes int styleResourceId,
                                 boolean isDefault){
            return addFlavor(name, styleResourceId, -1, isDefault);
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
            Flavor flavor = new Flavor(name, styleResourceId, dialogStyleResourceId);
            if(isDefault) defaultFlavor = flavor;
            return addFlavor(flavor);
        }

        public Builder addFlavor(Flavor... flavor){
            flavors.addAll(Arrays.asList(flavor));
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
