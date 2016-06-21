package com.ftinc.scoop.model;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/21/16.
 */

public abstract class IBinding {

    protected static final long DEFAULT_ANIMATION_DURATION = 600L;

    protected int toppingId;

    public IBinding(int toppingId){
        this.toppingId = toppingId;
    }

    public int getToppingId(){
        return toppingId;
    }

    abstract void update(Topping topping);

    abstract void unbind();

}
