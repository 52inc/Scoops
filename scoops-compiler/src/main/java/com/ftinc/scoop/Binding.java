package com.ftinc.scoop;

import com.squareup.javapoet.MethodSpec;

/**
 * Created by r0adkll on 6/26/16.
 */

abstract class Binding {

    final int id;

    Binding(int id){
        this.id = id;
    }

    abstract String getStatementFormat();
    abstract Object[] getStatementArguments();

    void brewStatement(MethodSpec.Builder method){
        method.addStatement(getStatementFormat(), getStatementArguments());
    }

}
