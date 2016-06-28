package com.ftinc.scoop.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import com.ftinc.scoop.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowTextView;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by r0adkll on 6/26/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TextViewColorAdapterTest {

    TextView mockTextView;
    TextViewColorAdapter adapter;

    @Before
    public void setup() throws Exception{
        adapter = new TextViewColorAdapter();
        Context ctx = RuntimeEnvironment.application.getApplicationContext();
        mockTextView = new TextView(ctx);
    }

    @Test
    public void applyColor() throws Exception {
        int color = Color.BLUE;
        adapter.applyColor(mockTextView, color);

        assertThat(mockTextView.getCurrentTextColor(), is(color));
    }

    @Test
    public void getColor() throws Exception {
        int color = Color.BLUE;
        adapter.applyColor(mockTextView, color);

        int _color = adapter.getColor(mockTextView);
        assertEquals(color, _color);
    }

}