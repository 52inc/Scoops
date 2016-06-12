package com.ftinc.scoop.ui;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatDelegate;

import com.ftinc.scoop.R;
import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.util.AttrUtils;
import com.ftinc.scoop.util.RecyclerViewMatcher;
import com.ftinc.scoop.util.ScoopsActivityTestRule;
import com.ftinc.scoop.util.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ftinc.scoop.util.TestUtils.withRecyclerView;
import static com.ftinc.scoop.util.TestUtils.withTextColor;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Created by r0adkll on 6/11/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScoopSettingsActivityTest {

    @Rule
    public ScoopsActivityTestRule<ScoopSettingsActivity> mActivityRule =
            new ScoopsActivityTestRule<>(ScoopSettingsActivity.class);

    @Test
    public void test_Inflation(){
        checkPositionInflation(0, "Default");
        checkPositionInflation(1, "Light");
        checkPositionInflation(2, "DayNight");
        checkPositionInflation(3, "Alternate 1");
        checkPositionInflation(4, "Alternate 2");
    }

    @Test
    public void test_DayNightSelection(){

        // Select the daynight position item
        onView(withId(R.id.recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        // Now verify that the daynight group options are exposed
        onView(withRecyclerView(R.id.recycler).atPositionOnView(2, R.id.daynight_options))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_DayNightModeChange(){

        // Select the daynight position item
        onView(withId(R.id.recycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withRecyclerView(R.id.recycler).atPositionOnView(2, R.id.opt_auto))
                .perform(click());

        assertThat(Scoop.getInstance().getDayNightMode(), is(AppCompatDelegate.MODE_NIGHT_AUTO));

        onView(withRecyclerView(R.id.recycler).atPositionOnView(2, R.id.opt_system))
                .perform(click());

        assertThat(Scoop.getInstance().getDayNightMode(), is(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));

        onView(withRecyclerView(R.id.recycler).atPositionOnView(2, R.id.opt_off))
                .perform(click());

        assertThat(Scoop.getInstance().getDayNightMode(), is(AppCompatDelegate.MODE_NIGHT_NO));

        onView(withRecyclerView(R.id.recycler).atPositionOnView(2, R.id.opt_on))
                .perform(click());

        assertThat(Scoop.getInstance().getDayNightMode(), is(AppCompatDelegate.MODE_NIGHT_YES));

    }

    private void checkPositionInflation(int position, String expectedText){
        onView(withRecyclerView(R.id.recycler)
                .atPositionOnView(position, R.id.title))
                .check(matches(withText(expectedText)));
    }

}