package com.ftinc.themeenginetest;

import android.app.Application;
import android.preference.PreferenceManager;

import com.ftinc.scoop.Scoop;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.themeenginetest
 * Created by drew.heavner on 6/8/16.
 */

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Scoop
        new Scoop.Builder()
                .addFlavor("Default", R.style.Theme_Scoop, true)
                .addFlavor("Light", R.style.Theme_Scoop_Light)
                .addFlavor("Alternate 1", R.style.Theme_Scoop_Alt1)
                .addFlavor("Alternate 2", R.style.Theme_Scoop_Alt2)
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize();

    }
}
