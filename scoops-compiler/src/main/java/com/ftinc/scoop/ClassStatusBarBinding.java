package com.ftinc.scoop;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeVariableName;

/**
 * Created by r0adkll on 6/26/16.
 */
final class ClassStatusBarBinding extends Binding{
    private static final ClassName STATUS_BAR_BINDING = ClassName.get("com.ftinc.scoop.binding", "StatusBarBinding");
    private static final ClassName IBINDING = ClassName.get("com.ftinc.scoop.binding", "IBinding");

    private final ClassName interpolator;
    private final long duration;

    public ClassStatusBarBinding(int id,
                                 ClassName interpolator,
                                 long duration) {
        super(id);
        this.interpolator = interpolator;
        this.duration = duration;
    }

    @Override
    public String getStatementFormat(){
        StringBuilder builder = new StringBuilder("bindings.add(new $T($L, target");
        if(interpolator != null) {
            builder.append(", new $T()");
        } else {
            builder.append(", null");
        }

        builder.append(", $L");

        builder.append("));");
        return builder.toString();
    }

    @Override
    public Object[] getStatementArguments(){
        int size = 3;
        int index = 0;
        if(interpolator != null) size++;

        Object[] args = new Object[size];
        args[index++] = STATUS_BAR_BINDING;
        args[index++] = id;
        if(interpolator != null){
            args[index++] = interpolator;
        }

        args[index] = duration == -1 ? IBINDING.toString().concat(".DEFAULT_ANIMATION_DURATION") : duration;
        return args;
    }

}
