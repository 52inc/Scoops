package com.ftinc.scoop.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.ftinc.scoop.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by r0adkll on 6/27/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DefaultColorAdapterTest {

    View view;
    DefaultColorAdapter adapter;

    @Before
    public void setUp() throws Exception {
        adapter = new DefaultColorAdapter();
        view = new View(RuntimeEnvironment.application.getApplicationContext());
    }

    @Test
    public void applyColor() throws Exception {
        int color = Color.GREEN;
        adapter.applyColor(view, color);

        Drawable bg = view.getBackground();
        assertTrue(bg instanceof ColorDrawable);
        ColorDrawable cbg = (ColorDrawable) bg;
        assertThat(cbg.getColor(), is(color));

    }

    @Test
    public void getColor() throws Exception {
        int color = Color.GREEN;
        adapter.applyColor(view, color);

        int appliedColor = adapter.getColor(view);
        assertThat(appliedColor, is(color));
    }

}