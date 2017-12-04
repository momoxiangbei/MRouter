package com.haha.mmxb.processor;

import com.google.auto.service.AutoService;
import com.haha.mmxb.annotation.Constant;
import com.haha.mmxb.annotation.Route;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.haha.mmxb.annotation.Constant.ROUTER_CLASS_NAME;
import static com.haha.mmxb.annotation.Constant.ROUTER_METHORD_NAME;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouterProcessor extends AbstractProcessor {
    private Elements mElementsUtil;
    private Filer mFiler;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportAnnotations = new HashSet<>();
        supportAnnotations.add(Route.class.getCanonicalName());
        return supportAnnotations;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementsUtil = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 组装参数 Map<String,Class<?>>> map
        ParameterSpec parameterSpec = ParameterSpec.builder(
                ParameterizedTypeName.get(
                        ClassName.get(Map.class),
                        ClassName.get(String.class),
                        ClassName.get(Class.class)
                ), "map").build();

        // 组装方法 @Override public void loadActivityMap
        MethodSpec.Builder routeMapMethod = MethodSpec.methodBuilder(ROUTER_METHORD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec);

        // 组装方法体 map.put("MyTestActivity", TestActivity.class);
        Set<? extends Element> routeElement = roundEnvironment.getElementsAnnotatedWith(Route.class);
        if (routeElement == null || routeElement.isEmpty()) {
            return true;
        }
        for (Element element : routeElement) {
            String[] routeValues = element.getAnnotation(Route.class).value();
            if (routeValues.length == 0) {
                continue;
            }
            for (String routeValue : routeValues) {
                // TODO: 2017/12/4 去重
                routeMapMethod.addStatement("map.put($S,$T.class)", routeValue, ClassName.get(element.asType()));
            }
        }

        // 生成java文件，即导航器Map文件
        TypeSpec typeSpec = TypeSpec.classBuilder(ROUTER_CLASS_NAME)
                .addSuperinterface(ClassName.get(mElementsUtil.getTypeElement(Constant.ROUTER_LOADER_NAME)))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(routeMapMethod.build())
                .build();

        JavaFile javaFile = JavaFile.builder(Constant.ROUTER_PACKAGE_NAME, typeSpec).build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
