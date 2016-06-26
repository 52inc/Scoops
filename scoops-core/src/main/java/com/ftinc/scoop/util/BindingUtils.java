package com.ftinc.scoop.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftinc.scoop.adapters.ColorAdapter;
import com.ftinc.scoop.adapters.DefaultColorAdapter;
import com.ftinc.scoop.adapters.ImageViewColorAdapter;
import com.ftinc.scoop.adapters.TextViewColorAdapter;
import com.ftinc.scoop.adapters.ViewGroupColorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by r0adkll on 6/26/16.
 */

public class BindingUtils {

    private static final Map<Class, ColorAdapter> COLOR_ADAPTERS = new HashMap<Class, ColorAdapter>(){
        {
            put(View.class, new DefaultColorAdapter());
            put(ViewGroup.class, new ViewGroupColorAdapter());
            put(TextView.class, new TextViewColorAdapter());
            put(ImageView.class, new ImageViewColorAdapter());
        }
    };

    private BindingUtils(){
        throw new IllegalAccessError("This class is not allowed to be instantiated");
    }

    public static <T extends View> ColorAdapter<T> getColorAdapter(Class<T> clazz){
        ColorAdapter adapter = COLOR_ADAPTERS.get(clazz);
        if(adapter == null){

            // Try super class
            adapter = COLOR_ADAPTERS.get(clazz.getSuperclass());
            if(adapter == null) {
                adapter = new DefaultColorAdapter();
            }
        }
        return adapter;
    }

}
