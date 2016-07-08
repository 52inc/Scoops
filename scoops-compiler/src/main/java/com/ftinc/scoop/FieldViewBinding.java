package com.ftinc.scoop;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents the processor brew into a
 * ViewBinding object in the generated code
 *
 * Created by r0adkll on 6/26/16.
 */
final class FieldViewBinding extends Binding{
    private static final ClassName VIEW_BINDING = ClassName.get("com.ftinc.scoop.binding", "ViewBinding");
    private static final ClassName BINDING_UTILS = ClassName.get("com.ftinc.scoop.util", "BindingUtils");
    private static final ClassName NO_ADAPTER = ClassName.get("com.ftinc.scoop.BindTopping", "NONE");

    private final String name;
    private final ClassName adapter;
    private final ClassName interpolator;

    public FieldViewBinding(int id,
                            String name,
                            ClassName adapter,
                            ClassName interpolator) {
        super(id);
        this.name = name;
        this.adapter = adapter;
        this.interpolator = interpolator;
    }

    @Override
    public String getStatementFormat(){
        StringBuilder builder = new StringBuilder("bindings.add(new $T($L, target.$L");
        if(adapter.equals(NO_ADAPTER)){
            builder.append(", $T.getColorAdapter(target.$L.getClass())");
        }else{
            builder.append(", new $T()");
        }

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
        List<Object> args = new ArrayList<>(6);
        args.add(VIEW_BINDING);
        args.add(id);
        args.add(name);

        if(adapter.equals(NO_ADAPTER)){
            args.add(BINDING_UTILS);
            args.add(name);
        }else{
            args.add(adapter);
        }

        if(interpolator != null){
            args.add(interpolator);
        }

        return args.toArray();
    }

}
