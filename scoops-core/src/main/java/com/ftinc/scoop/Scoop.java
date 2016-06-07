package com.ftinc.scoop;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.StyleRes;
import android.util.SparseIntArray;

import com.ftinc.scoop.model.Flavor;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Variables
     *
     */

    /**
     * Static mapping of all the available base application themes to use/apply
     * mapped by a developer defined ID
     */
    private SparseIntArray mThemeMapping = new SparseIntArray();

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


    private void initialize(Builder builder){
        // Validate builder
        if(builder.prefs != null && !builder.flavors.isEmpty()){

            mPreferences = builder.prefs;



        }
        throw new IllegalStateException("SharedPreferences and at least one flavor must be set");
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

        public Scoop build(){
            Scoop s = Scoop.getInstance();
            s.initialize(this);
            return s;
        }

    }



}
