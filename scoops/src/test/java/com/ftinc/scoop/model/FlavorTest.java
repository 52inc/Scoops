package com.ftinc.scoop.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/10/16.
 */
public class FlavorTest {

    private Random random;

    @Before
    public void setUp() throws Exception {
        random = new Random();
    }

    @Test
    public void getName() throws Exception {
        String name = "BaconBacon";
        Flavor flavor = new Flavor(name, -1);

        assertThat(flavor.getName(), is(name));
    }

    @Test
    public void getStyleResource() throws Exception {
        int styleRes = random.nextInt();
        Flavor flavor = new Flavor("", styleRes);

        assertThat(flavor.getStyleResource(), is(styleRes));
    }

    @Test
    public void getDialogStyleResource() throws Exception {
        int styleRes = random.nextInt();
        Flavor flavor = new Flavor("", -1, styleRes);

        assertThat(flavor.getDialogStyleResource(), is(styleRes));
    }

    @Test
    public void isDayNight() throws Exception {
        boolean value = random.nextBoolean();
        Flavor flavor = new Flavor("", -1, -1, value);

        assertThat(flavor.isDayNight(), is(value));
    }

    @Test
    public void equals() throws Exception {
        String name = "Bacon Bacon Bacon";
        int styleRes = random.nextInt();
        int dialogStyleRes = random.nextInt();
        boolean isDN = random.nextBoolean();

        Flavor f1 = new Flavor(name, styleRes, dialogStyleRes, isDN);
        Flavor f2 = new Flavor(name, styleRes, dialogStyleRes, isDN);
        assertTrue(f1.equals(f2));

        Flavor f3 = new Flavor(name, styleRes, dialogStyleRes);
        Flavor f4 = new Flavor(name, styleRes, dialogStyleRes);
        assertTrue(f3.equals(f4));

        Flavor f6 = new Flavor(name, styleRes);
        Flavor f7 = new Flavor(name, styleRes);
        assertTrue(f6.equals(f7));
    }

}