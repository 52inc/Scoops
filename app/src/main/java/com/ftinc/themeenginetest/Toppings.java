package com.ftinc.themeenginetest;

import com.ftinc.scoop.Scoop;
import com.ftinc.scoop.model.Topping;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.themeenginetest
 * Created by drew.heavner on 6/17/16.
 */

public class Toppings {

    static class Primary extends Topping{
        public static Primary create(){
            return new Primary();
        }
        public Primary() {
            super("Primary");
        }
    }

    public static class PrimaryDark extends Topping{
        public static PrimaryDark create(){
            return new PrimaryDark();
        }
        public PrimaryDark(){
            super("Primary Dark");
        }
    }


    public static void add(){
        Scoop.sugarCone()
                .addTopping(Primary.create())
                .addTopping(PrimaryDark.create());
    }

}
