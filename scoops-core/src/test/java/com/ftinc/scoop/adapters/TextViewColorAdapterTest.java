package com.ftinc.scoop.adapters;

import android.widget.TextView;

import com.ftinc.scoop.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by r0adkll on 6/26/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TextViewColorAdapterTest {

    TextViewColorAdapter adapter;

    @Before
    public void setup() throws Exception{
        adapter = new TextViewColorAdapter();
        
    }

    @Test
    public void applyColor() throws Exception {



    }

    @Test
    public void getColor() throws Exception {

    }

}