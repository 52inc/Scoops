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
public class Topping {

    /***********************************************************************************************
     *
     * Variables
     *
     */

    final int id;
    final String name;

    @ColorInt
    int color;

    @ColorInt
    int previousColor;

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
        color = previousColor = defaultColor;
    }

    /***********************************************************************************************
     *
     * Getters
     *
     */

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getPreviousColor() {
        return previousColor;
    }

    public void updateColor(@ColorInt int color){
        this.previousColor = this.color;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topping topping = (Topping) o;

        if (id != topping.id) return false;
        if (color != topping.color) return false;
        if (previousColor != topping.previousColor) return false;
        return name != null ? name.equals(topping.name) : topping.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + color;
        result = 31 * result + previousColor;
        return result;
    }

    @Override
    public String toString() {
        return "Topping{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", previousColor=" + previousColor +
                '}';
    }
}
