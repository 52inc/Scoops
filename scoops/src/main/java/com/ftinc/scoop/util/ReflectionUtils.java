package com.ftinc.scoop.util;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.util
 * Created by drew.heavner on 6/24/16.
 */

public class ReflectionUtils {

    private ReflectionUtils(){
        throw new IllegalAccessError("This class is not allowed to be instantiated");
    }

    public static boolean isTypeOf(Class<?> myClass, Class<?> superClass) {
        boolean isSubclassOf = false;
        if (!myClass.equals(superClass)) {
            myClass = myClass.getSuperclass();
            isSubclassOf = isTypeOf(myClass, superClass);
        } else {
            isSubclassOf = true;
        }

        return isSubclassOf;
    }

}
