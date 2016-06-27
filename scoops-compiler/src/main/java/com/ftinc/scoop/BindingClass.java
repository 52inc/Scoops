package com.ftinc.scoop;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by r0adkll on 6/26/16.
 */

final class BindingClass {

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final ClassName TOPPING_BINDER = ClassName.get("com.ftinc.scoop.internal", "ToppingBinder");
    private static final ClassName IBINDING = ClassName.get("com.ftinc.scoop.binding", "IBinding");
    private static final ClassName LIST = ClassName.get("java.util", "List");
    private static final ClassName ARRAY_LIST = ClassName.get("java.util", "ArrayList");
    private static final ParameterizedTypeName listOfIBinding = ParameterizedTypeName.get(LIST, IBINDING);

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private ClassStatusBarBinding statusBarBinding;
    private List<FieldViewBinding> fieldBindings;
    private ClassName binderClassName;
    private TypeName targetTypeName;

    public BindingClass(TypeName targetTypeName, ClassName binderClassName){
        this.binderClassName = binderClassName;
        this.targetTypeName = targetTypeName;

        fieldBindings = new ArrayList<>();
    }

    public void setStatusBarBinding(ClassStatusBarBinding binding){
        statusBarBinding = binding;
    }

    public void addViewBinding(FieldViewBinding binding){
        fieldBindings.add(binding);
    }

    public JavaFile brewJava(){
        TypeSpec.Builder result = TypeSpec.classBuilder(binderClassName)
                .addModifiers(PUBLIC, FINAL)
                .addSuperinterface(ParameterizedTypeName.get(TOPPING_BINDER, targetTypeName));

        result.addMethod(createBindMethod(targetTypeName));

        return JavaFile.builder(binderClassName.packageName(), result.build())
                .addFileComment("Generated code from Scoops. Do not modify!")
                .build();
    }

    private MethodSpec createBindMethod(TypeName targetType) {
        MethodSpec.Builder result = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(listOfIBinding)
                .addParameter(targetType, "target");

        // Add list variable to store all generated bindings in
        result.addStatement("$T bindings = new $T<>()", listOfIBinding, ARRAY_LIST);
        result.addCode("\n");

        // if status bar binding exists, generate statement
        if(statusBarBinding != null){
            statusBarBinding.brewStatement(result);
        }

        result.addCode("\n");

        // Generate all the view bindings
        for (FieldViewBinding fieldBinding : fieldBindings) {
            fieldBinding.brewStatement(result);
        }

        result.addCode("\n")
              .addStatement("return bindings");

        return result.build();
    }



}
