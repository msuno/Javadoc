package cn.msuno.javadoc.build;

import static cn.msuno.javadoc.build.RuntimeJavadocHelper.elementDocFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.elementNameFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.elementTypeFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.enumConstantsFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.fieldsFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.methodsFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.paramTypesFieldName;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * @author msuno
 */
public class JsonJavadocBuilder {
    
    private final ProcessingEnvironment processingEnv;
    
    JsonJavadocBuilder(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }
    
    JSONObject getClassJavadocAsJsonOrNull(TypeElement classElement) {
        String classDoc = processingEnv.getElementUtils().getDocComment(classElement);
        
        if (StrUtil.isBlank(classDoc)) {
            classDoc = StrUtil.EMPTY;
        }
        
        Map<ElementKind, List<Element>> children = new HashMap<>();
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (!children.containsKey(enclosedElement.getKind())) {
                children.put(enclosedElement.getKind(), new ArrayList<>());
            }
            children.get(enclosedElement.getKind()).add(enclosedElement);
        }
        
        final List<Element> emptyList = Collections.emptyList();
        List<Element> enclosedFields = defaultIfNull(children.get(FIELD), emptyList);
        List<Element> enclosedEnumConstants = defaultIfNull(children.get(ENUM_CONSTANT), emptyList);
        List<Element> enclosedMethods = defaultIfNull(children.get(METHOD), emptyList);
        
        JSONArray fieldDocs = getJavadocAsJson(enclosedFields, new FieldJavadocAsJson());
        JSONArray enumConstantDocs = getJavadocAsJson(enclosedEnumConstants, new FieldJavadocAsJson());
        JSONArray methodDocs = getJavadocAsJson(enclosedMethods, new MethodJavadocAsJson());
        
        if (StrUtil.isBlank(classDoc) && fieldDocs.isEmpty() && enumConstantDocs.isEmpty() && methodDocs.isEmpty()) {
            return null;
        }
        
        JSONObject json = new JSONObject();
        json.set(elementDocFieldName(), classDoc);
        json.set(fieldsFieldName(), fieldDocs);
        json.set(enumConstantsFieldName(), enumConstantDocs);
        json.set(methodsFieldName(), methodDocs);
        return json;
    }
    
    private static JSONArray getJavadocAsJson(List<Element> elements, ElementToJsonFunction createDoc) {
        JSONArray jsonArray = new JSONArray();
        for (Element e : elements) {
            JSONObject eMapped = createDoc.apply(e);
            if (eMapped != null) {
                jsonArray.add(eMapped);
            }
        }
        return jsonArray;
    }
    
    private interface ElementToJsonFunction {
        JSONObject apply(Element e);
    }
    
    private class FieldJavadocAsJson implements ElementToJsonFunction {
        @Override
        public JSONObject apply(Element field) {
            String javadoc = processingEnv.getElementUtils().getDocComment(field);
            if (StrUtil.isBlank(javadoc)) {
                return null;
            }
            JSONObject jsonDoc = new JSONObject();
            jsonDoc.set(elementNameFieldName(), field.getSimpleName().toString());
            jsonDoc.set(elementDocFieldName(), javadoc);
            jsonDoc.set(elementTypeFieldName(), field.asType().toString());
            return jsonDoc;
        }
    }
    
    private class MethodJavadocAsJson implements ElementToJsonFunction {
        @Override
        public JSONObject apply(Element method) {
            assert method instanceof ExecutableElement;
            
            String methodJavadoc = processingEnv.getElementUtils().getDocComment(method);
            if (StrUtil.isBlank(methodJavadoc)) {
                return null;
            }
            
            JSONObject jsonDoc = new JSONObject();
            jsonDoc.set(elementNameFieldName(), method.getSimpleName().toString());
            jsonDoc.set(paramTypesFieldName(), getParamErasures((ExecutableElement) method));
            jsonDoc.set(elementDocFieldName(), methodJavadoc);
            return jsonDoc;
        }
    }
    
    private JSONArray getParamErasures(ExecutableElement executableElement) {
        Types typeUtils = processingEnv.getTypeUtils();
        
        final JSONArray jsonValues = new JSONArray();
        for (VariableElement parameter : executableElement.getParameters()) {
            TypeMirror erasure = typeUtils.erasure(parameter.asType());
            jsonValues.add(erasure.toString());
        }
        return jsonValues;
    }
    
    private static <T> T defaultIfNull(T actualValue, T defaultValue) {
        return actualValue != null ? actualValue : defaultValue;
    }
}
