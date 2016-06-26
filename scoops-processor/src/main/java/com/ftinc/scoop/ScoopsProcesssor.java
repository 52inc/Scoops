package com.ftinc.scoop;

import android.view.animation.LinearInterpolator;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public final class ScoopsProcesssor extends AbstractProcessor{

    static final String VIEW_TYPE = "android.view.View";
    static final String ACTIVITY_TYPE = "android.app.Activity";

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(
                Arrays.asList(
                        BindScoop.class.getCanonicalName(),
                        BindScoopStatus.class.getCanonicalName()
                )
        );
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<TypeElement, BindingClass> targetClassMap = findAndParseTargets(roundEnvironment);

        for (Map.Entry<TypeElement, BindingClass> entry : targetClassMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            BindingClass bindingClass = entry.getValue();

            JavaFile javaFile = bindingClass.brewJava();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write view binder for type %s: %s", typeElement,
                        e.getMessage());
            }
        }

        return true;
    }

    private Map<TypeElement, BindingClass> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, BindingClass> targetClassMap = new LinkedHashMap<>();
        Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

        // Process each @BindScoop class
        for (Element element : env.getElementsAnnotatedWith(BindScoop.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseBindScoop(element, targetClassMap);
            } catch (Exception e) {
                logParsingError(element, BindScoop.class, e);
            }
        }

        // Process each @BindScoopStatus class
        for (Element element : env.getElementsAnnotatedWith(BindScoopStatus.class)) {
            if(!SuperficialValidation.validateElement(element)) continue;
            try{
                parseBindScoopStatus(element, targetClassMap);
            }catch (Exception e){
                logParsingError(element, BindScoopStatus.class, e);
            }
        }

        return targetClassMap;
    }

    private void parseBindScoop(Element element, Map<TypeElement, BindingClass> targetClassMap){
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        // Start by verifying common generated code restrictions.
        boolean hasError = isInaccessibleViaGeneratedCode(BindScoop.class, "fields", element)
                || isBindingInWrongPackage(BindScoop.class, element);

        // Verify that the target type extends from View.
        TypeMirror elementType = element.asType();
        if (elementType.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVariable = (TypeVariable) elementType;
            elementType = typeVariable.getUpperBound();
        }
        if (!isSubtypeOfType(elementType, VIEW_TYPE) && !isInterface(elementType)) {
            error(element, "@%s fields must extend from View or be an interface. (%s.%s)",
                    BindScoop.class.getSimpleName(), enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        if (hasError) {
            return;
        }

        BindScoop annotation = element.getAnnotation(BindScoop.class);

        // Start assembling binding information
        BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);

        String name = element.getSimpleName().toString();
        TypeName type = TypeName.get(elementType);

        ClassName adapter = className(asTypeElement(getAdapterTypeMirror(annotation)));
        ClassName interpolator = className(asTypeElement(getInterpolatorTypeMirror(annotation)));

        FieldViewBinding binding = new FieldViewBinding(annotation.value(), name, adapter, interpolator);
        bindingClass.addViewBinding(binding);

    }

    private void parseBindScoopStatus(Element element, Map<TypeElement, BindingClass> targetClassMap){

        // Verify that element is of type Class
        if(element.getKind() != ElementKind.CLASS){
            error(element, "Only classes can be annotated with %s", BindScoopStatus.class.getSimpleName());
        }else{

            // Start by verifying common generated code restrictions.
            boolean hasError = false;

            // Verify that the target type extends from View.
            TypeMirror elementType = element.asType();
            if (elementType.getKind() == TypeKind.TYPEVAR) {
                TypeVariable typeVariable = (TypeVariable) elementType;
                elementType = typeVariable.getUpperBound();
            }
            if (!isSubtypeOfType(elementType, ACTIVITY_TYPE) && !isInterface(elementType)) {
                error(element, "@%s classes must extend from Activity(%s.%s)",
                        BindScoopStatus.class.getSimpleName(), element.getSimpleName());
                hasError = true;
            }

            if (hasError) {
                return;
            }

            BindScoopStatus annotation = element.getAnnotation(BindScoopStatus.class);

            // Start assembling binding information
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, (TypeElement) element);

            ClassName interpolator = className(asTypeElement(getInterpolatorTypeMirror(annotation)));

            ClassStatusBarBinding binding = new ClassStatusBarBinding(annotation.value(), interpolator);
            bindingClass.setStatusBarBinding(binding);
        }

    }

    private BindingClass getOrCreateTargetClass(Map<TypeElement, BindingClass> targetClassMap,
                                                TypeElement enclosingElement) {
        BindingClass bindingClass = targetClassMap.get(enclosingElement);
        if (bindingClass == null) {
            TypeName targetType = TypeName.get(enclosingElement.asType());
            if (targetType instanceof ParameterizedTypeName) {
                targetType = ((ParameterizedTypeName) targetType).rawType;
            }

            String packageName = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, packageName);
            ClassName binderClassName = ClassName.get(packageName, className + "_ToppingBinder");

            bindingClass = new BindingClass(targetType, binderClassName);
            targetClassMap.put(enclosingElement, bindingClass);
        }
        return bindingClass;
    }

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    private ClassName className(TypeElement element){
        if(element != null){
            return ClassName.get(element);
        }
        return null;
    }

    private TypeElement asTypeElement(TypeMirror typeMirror) {
        if(typeMirror != null) {
            return (TypeElement) typeUtils.asElement(typeMirror);
        }
        return null;
    }

    private TypeMirror getAdapterTypeMirror(BindScoop annotation){
        TypeMirror value = null;
        try {
            annotation.adapter();
        }catch (MirroredTypeException e){
            value = e.getTypeMirror();
        }
        return value;
    }

    private TypeMirror getInterpolatorTypeMirror(BindScoop annotation){
        TypeMirror value = null;
        try{
            annotation.interpolator();
        }catch (MirroredTypeException e){
            value = e.getTypeMirror();
        }

        return value;
    }

    private TypeMirror getInterpolatorTypeMirror(BindScoopStatus annotation){
        TypeMirror value = null;
        try{
            annotation.interpolator();
        }catch (MirroredTypeException e){
            value = e.getTypeMirror();
        }

        return value;
    }

    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass,
                                                   String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        // Verify method modifiers.
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error(element, "@%s %s must not be private or static. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing type.
        if (enclosingElement.getKind() != CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing class visibility is not private.
        if (enclosingElement.getModifiers().contains(PRIVATE)) {
            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        return hasError;
    }

    private boolean isBindingInWrongPackage(Class<? extends Annotation> annotationClass,
                                            Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String qualifiedName = enclosingElement.getQualifiedName().toString();

        if (qualifiedName.startsWith("android.")) {
            error(element, "@%s-annotated class incorrectly in Android framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            return true;
        }
        if (qualifiedName.startsWith("java.")) {
            error(element, "@%s-annotated class incorrectly in Java framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            return true;
        }

        return false;
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation,
                                 Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private boolean isInterface(TypeMirror typeMirror) {
        return typeMirror instanceof DeclaredType
                && ((DeclaredType) typeMirror).asElement().getKind() == INTERFACE;
    }

    private boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (otherType.equals(typeMirror.toString())) {
            return true;
        }
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }
        Element element = declaredType.asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = (TypeElement) element;
        TypeMirror superType = typeElement.getSuperclass();
        if (isSubtypeOfType(superType, otherType)) {
            return true;
        }
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }
        return false;
    }
}
