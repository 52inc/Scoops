package com.ftinc.scoop;

import com.squareup.javapoet.ClassName;

/**
 * Created by r0adkll on 6/26/16.
 */

final class BindingClass {

    private static final ClassName TOPPING_BINDER = ClassName.get("com.ftinc.scoop.internal", "ToppingBinder");
    private static final ClassName VIEW_BINDING = ClassName.get("com.ftinc.scoop.binding", "ViewBinding");
    private static final ClassName STATUS_BAR_BINDING = ClassName.get("com.ftinc.scoop.binding", "StatusBarBinding");

}
