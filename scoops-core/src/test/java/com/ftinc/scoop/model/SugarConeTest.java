package com.ftinc.scoop.model;

import android.util.SparseArray;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/24/16.
 */
@RunWith(RobolectricTestRunner.class)
public class SugarConeTest {

    private SugarCone mSugarCone;

    @Before
    public void setUp() throws Exception {
        mSugarCone = new SugarCone();
    }

    @Test
    public void bind() throws Exception {

        // Mock a topping, then add to sugar cone
        Topping topping = mock(Topping.class);
        when(topping.getId()).thenReturn(0);
        mSugarCone.addTopping(topping);

        // Mock Binding
        IBinding binding = mock(IBinding.class);

        // Now bind
        mSugarCone.bind(this, topping.getId(), binding);

        // Now verify that binding was created
        Set<IBinding> bindings = mSugarCone.getBindings(getClass());

        assertTrue(bindings.contains(binding));
    }

    @Test
    public void unbind() throws Exception {

        // Mock a topping, then add to sugar cone
        Topping topping = mock(Topping.class);
        when(topping.getId()).thenReturn(0);
        mSugarCone.addTopping(topping);

        // Mock Binding
        IBinding binding = mock(IBinding.class);

        // Now bind
        mSugarCone.bind(this, topping.getId(), binding);

        // Now verify that binding was created
        Set<IBinding> bindings = mSugarCone.getBindings(getClass());
        assertTrue(bindings.contains(binding));

        mSugarCone.unbind(this);

        Set<IBinding> unbindings = mSugarCone.getBindings(getClass());
        assertTrue(!unbindings.contains(binding));
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void getColorAdapter() throws Exception {

    }

    @Test
    public void addToppings() throws Exception {
        Topping t1 = mock(Topping.class);
        Topping t2 = mock(Topping.class);

        when(t1.getId()).thenReturn(0);
        when(t1.getName()).thenReturn("topping1");
        when(t2.getId()).thenReturn(1);
        when(t2.getName()).thenReturn("topping2");

        mSugarCone.addToppings(Arrays.asList(t1, t2));

        SparseArray<Topping> toppings = mSugarCone.getToppings();

        Topping v1 = toppings.get(0);
        Topping v2 = toppings.get(1);

        assertNotNull(v1);
        assertNotNull(v2);
    }

}