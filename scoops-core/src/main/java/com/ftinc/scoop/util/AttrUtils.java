package com.ftinc.scoop.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;

import com.ftinc.scoop.R;

import org.w3c.dom.Attr;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.util
 * Created by drew.heavner on 6/8/16.
 */

public class AttrUtils {

    private AttrUtils(){
        throw new IllegalAccessError("This class is not allowed to be instantiated");
    }

    /**
     * Get the selectableItemBackground attribute drawable
     * @return
     */
    public static Drawable getSelectableItemBackground(Context ctx){
        int[] attrs = new int[] { R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = ctx.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();
        return drawableFromTheme;
    }

    /**
     * Get the selectableItemBackground attribute drawable
     * @return
     */
    public static Drawable getSelectableItemBackgroundBorderless(Context ctx){
        int[] attrs = new int[] { R.attr.selectableItemBackgroundBorderless /* index 0 */};
        TypedArray ta = ctx.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();
        return drawableFromTheme;
    }

    /**
     * Get the color value for give attribute
     */
    @ColorInt
    public static int getColorAttr(Context ctx, @AttrRes int colorAttrId){
        int[] attrs = new int[] { colorAttrId /* index 0 */};
        TypedArray ta = ctx.obtainStyledAttributes(attrs);
        int colorFromTheme = ta.getColor(0, 0);
        ta.recycle();
        return colorFromTheme;
    }

    /**
     * Get color from a specified style resource for a specified Id
     */
    @ColorInt
    public static int getColorAttr(Context ctx, @StyleRes int styleResId, @AttrRes int colorAttrId){
        TypedArray a = ctx.obtainStyledAttributes(styleResId, new int[]{colorAttrId});
        int colorFromTheme = a.getColor(0, 0);
        a.recycle();
        return colorFromTheme;
    }

    /**
     * Get a Drawable attribute value
     */
    public static Drawable getDrawableAttr(Context ctx, @AttrRes int drawableAttrId){
        int[] attrs = new int[] { drawableAttrId /* index 0 */};
        TypedArray ta = ctx.obtainStyledAttributes(attrs);
        Drawable drawable = ta.getDrawable(0);
        ta.recycle();
        return drawable;
    }

    @StyleRes
    public static int getResourceAttr(Context ctx, @AttrRes int resourceAttrId){
        TypedArray a = ctx.obtainStyledAttributes(new int[]{resourceAttrId});
        int resourceId = a.getResourceId(0, -1);
        a.recycle();
        return resourceId;
    }

}
