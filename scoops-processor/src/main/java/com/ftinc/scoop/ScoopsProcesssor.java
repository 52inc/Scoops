package com.ftinc.scoop;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public class ScoopsProcesssor extends AbstractProcessor{

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(
                Arrays.asList(
                        BindScoop.class
                )
        )
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }
}
