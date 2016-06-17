package com.ftinc.scoop.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.util
 * Created by drew.heavner on 6/8/16.
 */

public class Utils {

    private Utils(){
        throw new IllegalAccessError("This class is not allowed to be instantiated");
    }

    public static float dpToPx(Context ctx, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

    public static int dipToPx(Context ctx, float dp){
        return (int) dpToPx(ctx, dp);
    }

    public static ColorStateList colorToStateList(@ColorInt int color){
        return new ColorStateList(new int[][]{new int[0]}, new int[]{color});
    }

}
