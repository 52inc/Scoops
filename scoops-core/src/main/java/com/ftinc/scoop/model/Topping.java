package com.ftinc.scoop.model;

import android.support.annotation.ColorInt;

/**
 * Model object that represents a Color Property that the developer can subscribe to changes for
 * to update specified views.
 *
 * Example:
 *
 *  Say you have a {@link Topping} that represents a PRIMARY color that you want to use for toolbars, fabs, and the like.
 *  and you want to update this color using Palette based on primary images loading (such as a banner) and you want bound
 *  views to automatically react to these changes. The user would then bind the associated views to this property
 *  and Scoops will automatically take care of it
 *
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/17/16.
 */
public abstract class Topping {

    /***********************************************************************************************
     *
     * Variables
     *
     */

    final int id;
    final String name;

    @ColorInt
    int currentColor;

    @ColorInt
    int lastColor;

    /***********************************************************************************************
     *
     * Constructors
     *
     */

    public Topping(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Topping(int id, String name, int defaultColor){
        this(id, name);
        currentColor = lastColor = defaultColor;
    }

    /***********************************************************************************************
     *
     * Getters
     *
     */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public int getLastColor() {
        return lastColor;
    }


}
