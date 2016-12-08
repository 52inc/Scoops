package com.ftinc.scoop.binding;

import com.ftinc.scoop.Topping;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/21/16.
 */

public abstract class IBinding {

    public static final long DEFAULT_ANIMATION_DURATION = 600L;

    protected int toppingId;

    public IBinding(int toppingId){
        this.toppingId = toppingId;
    }

    public int getToppingId(){
        return toppingId;
    }

    public abstract void update(Topping topping);

    public abstract void unbind();

}
