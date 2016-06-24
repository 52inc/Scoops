package com.ftinc.themeenginetest;

import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.model.Topping;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.themeenginetest
 * Created by drew.heavner on 6/17/16.
 */

public class Toppings {

    public static final int PRIMARY = 0;
    public static final int PRIMARY_DARK = 1;
    public static final int ACCENT = 2;

    public static Topping[] getToppings(){
        return new Topping[]{
                new Topping(PRIMARY, "Primary"),
                new Topping(PRIMARY_DARK, "Primary Dark"),
                new Topping(ACCENT, "Accent")
        };
    }

}
