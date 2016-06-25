package com.ftinc.themeenginetest;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.ftinc.scoop.Scoop;

import timber.log.Timber;

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
        Scoop.waffleCone()
                .addFlavor("Default", R.style.Theme_Scoop, true)
                .addFlavor("Light", R.style.Theme_Scoop_Light)
                .addDayNightFlavor("DayNight", R.style.Theme_Scoop_DayNight)
                .addFlavor("Alternate 1", R.style.Theme_Scoop_Alt1)
                .addFlavor("Alternate 2", R.style.Theme_Scoop_Alt2)
                .addToppings(Toppings.getToppings())
                .setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this))
                .initialize();

        // Plant the logger
        Timber.plant(new Timber.DebugTree());

    }
}
