package com.ftinc.scoop.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.ftinc.scoop.R;
import com.ftinc.scoop.Scoop;

/**
 * Created by r0adkll on 6/11/16.
 */

public class ScoopsActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

    private static final String PREFERENCE_NAME = "ScoopSettingsActivityTest_prefs";

    public ScoopsActivityTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        Context ctx = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Initialize Scoop
        Scoop.waffleCone()
                .addFlavor("Default", R.style.Theme_Scoop, true)
                .addFlavor("Light", R.style.Theme_Scoop_Light)
                .addDayNightFlavor("DayNight", R.style.Theme_Scoop_DayNight)
                .addFlavor("Alternate 1", R.style.Theme_Scoop_Alt1)
                .addFlavor("Alternate 2", R.style.Theme_Scoop_Alt2)
                .setSharedPreferences(prefs)
                .initialize();
    }
}
