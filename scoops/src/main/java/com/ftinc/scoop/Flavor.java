package com.ftinc.scoop;

import android.support.annotation.StyleRes;

/**
 * This object represents a styled 'Theme' from the resource attributes
 *
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/7/16.
 */
public final class Flavor {

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private final String mName;
    private final int mStyleResource;
    private final int mDialogStyleResource;
    private final boolean mIsDayNight;

    /***********************************************************************************************
     *
     * Constructors
     *
     */

    public Flavor(String name, int styleResource){
        this(name, styleResource, -1);
    }

    public Flavor(String name, int styleResource, int dialogStyleResource){
        this(name, styleResource, dialogStyleResource, false);
    }

    public Flavor(String name, int styleResource, int dialogStyleResource, boolean isDayNight){
        mName = name;
        mStyleResource = styleResource;
        mDialogStyleResource = dialogStyleResource;
        mIsDayNight = isDayNight;
    }
    
    /***********************************************************************************************
     * 
     * Getters
     * 
     */

    public String getName() {
        return mName;
    }

    @StyleRes
    public int getStyleResource() {
        return mStyleResource;
    }

    @StyleRes
    public int getDialogStyleResource(){
        return mDialogStyleResource;
    }

    public boolean isDayNight() {
        return mIsDayNight;
    }

    /***********************************************************************************************
     *
     * Base Methods
     *
     */

    @Override
    public String toString() {
        return "Flavor{" +
                "mName='" + mName + '\'' +
                ", mStyleResource=" + mStyleResource +
                ", mDialogStyleResource=" + mDialogStyleResource +
                ", mIsDayNight=" + mIsDayNight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flavor flavor = (Flavor) o;

        if (mStyleResource != flavor.mStyleResource) return false;
        if (mDialogStyleResource != flavor.mDialogStyleResource) return false;
        if (mIsDayNight != flavor.mIsDayNight) return false;
        return mName != null ? mName.equals(flavor.mName) : flavor.mName == null;

    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + mStyleResource;
        result = 31 * result + mDialogStyleResource;
        result = 31 * result + (mIsDayNight ? 1 : 0);
        return result;
    }
}
