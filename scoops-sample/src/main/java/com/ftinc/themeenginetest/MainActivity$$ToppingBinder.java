package com.ftinc.themeenginetest;

import android.app.Activity;
import android.view.animation.AccelerateInterpolator;

import com.ftinc.scoop.adapters.DefaultColorAdapter;
import com.ftinc.scoop.binding.IBinding;
import com.ftinc.scoop.binding.StatusBarBinding;
import com.ftinc.scoop.binding.ViewBinding;
import com.ftinc.scoop.internal.ToppingBinder;
import com.ftinc.themeenginetest.adapters.ButtonColorAdapter;
import com.ftinc.themeenginetest.adapters.FABColorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r0adkll on 6/25/16.
 */

public class MainActivity$$ToppingBinder<T extends MainActivity> implements ToppingBinder<T> {

    @Override
    public List<IBinding> bind(T target) {
        List<IBinding> bindings = new ArrayList<>();

        // Generate StatusBar Binding
        bindings.add(new StatusBarBinding(0, target, null));

        // Add View Bindings
        bindings.add(new ViewBinding(0, target.mAppBar, new DefaultColorAdapter(), null));
        bindings.add(new ViewBinding(2, target.mFab, new FABColorAdapter(), new AccelerateInterpolator()));
        bindings.add(new ViewBinding(2, target.mColoredButton, new ButtonColorAdapter(), null));

        return bindings;
    }

}
