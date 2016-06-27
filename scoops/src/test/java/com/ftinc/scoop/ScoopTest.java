package com.ftinc.scoop;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.ftinc.scoop.model.Flavor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop
 * Created by drew.heavner on 6/10/16.
 */
@SuppressWarnings("ResourceType")
@RunWith(MockitoJUnitRunner.class)
public class ScoopTest {

    private static final Flavor[] TEST_FLAVORS = new Flavor[]{
            new Flavor("Bacon 1", 1),
            new Flavor("Bacon 2", 2, 1),
            new Flavor("Bacon 3", 3, 2, true)
    };

    private static final int NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_AUTO;

    @Mock
    SharedPreferences mMockSharedPreferences;

    @Mock
    SharedPreferences.Editor mMockSharedPreferencesEditor;

    @Before
    public void setUp() throws Exception {

        when(mMockSharedPreferences.getInt(eq(Scoop.PREFERENCE_DAYNIGHT_KEY), anyInt()))
                .thenReturn(NIGHT_MODE);

        when(mMockSharedPreferences.getInt(eq(Scoop.PREFERENCE_FLAVOR_KEY), anyInt()))
                .thenReturn(1);

        when(mMockSharedPreferencesEditor.commit())
                .thenReturn(true);

        when(mMockSharedPreferencesEditor.putInt(eq(Scoop.PREFERENCE_FLAVOR_KEY), anyInt()))
                .thenReturn(mMockSharedPreferencesEditor);

        when(mMockSharedPreferencesEditor.putInt(eq(Scoop.PREFERENCE_DAYNIGHT_KEY), anyInt()))
                .thenReturn(mMockSharedPreferencesEditor);

        when(mMockSharedPreferences.edit())
                .thenReturn(mMockSharedPreferencesEditor);

        Scoop.waffleCone()
                .addFlavor(TEST_FLAVORS)
                .setSharedPreferences(mMockSharedPreferences)
                .initialize();
    }

    @Test
    public void getDayNightMode() throws Exception {
        int mode = Scoop.getInstance().getDayNightMode();
        assertThat(mode, is(NIGHT_MODE));
    }

    @Test
    public void getFlavors() throws Exception {
        List<Flavor> flavors = Scoop.getInstance().getFlavors();
        List<Flavor> testFlavors = Arrays.asList(TEST_FLAVORS);

        assertEquals(flavors, testFlavors);
    }

    @Test
    public void getCurrentFlavor() throws Exception {
        Flavor currentFlavor = Scoop.getInstance().getCurrentFlavor();
        assertEquals(currentFlavor, TEST_FLAVORS[1]);
    }

    @Test
    public void choose() throws Exception {
        int choice = 2;
        when(mMockSharedPreferences.getInt(eq(Scoop.PREFERENCE_FLAVOR_KEY), anyInt()))
                .thenReturn(choice);

        Flavor choiceFlavor = TEST_FLAVORS[choice];
        Scoop.getInstance().choose(choiceFlavor);

        Flavor currentFlavor = Scoop.getInstance().getCurrentFlavor();

        assertEquals(choiceFlavor, currentFlavor);
    }

}