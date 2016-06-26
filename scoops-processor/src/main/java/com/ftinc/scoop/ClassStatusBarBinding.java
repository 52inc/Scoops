package com.ftinc.scoop;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

/**
 * Created by r0adkll on 6/26/16.
 */
final class ClassStatusBarBinding extends Binding{
    private static final ClassName STATUS_BAR_BINDING = ClassName.get("com.ftinc.scoop.binding", "StatusBarBinding");

    private final ClassName interpolator;

    public ClassStatusBarBinding(int id,
                                 ClassName interpolator) {
        super(id);
        this.interpolator = interpolator;
    }

    @Override
    public String getStatementFormat(){
        StringBuilder builder = new StringBuilder("bindings.add(new $T($L, target");
        if(interpolator != null) {
            builder.append(", new $T()");
        } else {
            builder.append(", null");
        }
        builder.append("));");
        return builder.toString();
    }

    @Override
    public Object[] getStatementArguments(){
        int size = 2;
        if(interpolator != null) size++;

        Object[] args = new Object[size];
        args[0] = STATUS_BAR_BINDING;
        args[1] = id;
        if(interpolator != null){
            args[2] = interpolator;
        }

        return args;
    }

}
