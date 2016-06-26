package com.ftinc.scoop;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

/**
 * This represents the processor brew into a
 * ViewBinding object in the generated code
 *
 * Created by r0adkll on 6/26/16.
 */
final class FieldViewBinding {
    private static final ClassName VIEW_BINDING = ClassName.get("com.ftinc.scoop.binding", "ViewBinding");

    private final int id;
    private final String name;
    private final ClassName adapter;
    private final ClassName interpolator;

    public FieldViewBinding(int id,
                            String name,
                            ClassName adapter,
                            ClassName interpolator) {
        this.id = id;
        this.name = name;
        this.adapter = adapter;
        this.interpolator = interpolator;
    }

    private String getStatementFormat(){
        StringBuilder builder = new StringBuilder("bindings.add(new $T($L, target.$L, new $T()");
        if(interpolator != null) {
            builder.append(", new $T()");
        } else {
            builder.append(", null");
        }
        builder.append("));");
        return builder.toString();
    }

    private Object[] getStatementArguments(){
        int size = 4;
        if(interpolator != null) size++;

        Object[] args = new Object[size];
        args[0] = VIEW_BINDING;
        args[1] = id;
        args[2] = name;
        args[3] = adapter;
        if(interpolator != null){
            args[4] = interpolator;
        }

        return args;
    }

    public com.squareup.javapoet.MethodSpec.Builder brewStatement(MethodSpec.Builder method){
        return method.addStatement(getStatementFormat(), getStatementArguments());
    }

}
