package cn.msuno.javadoc.parse;


import static cn.msuno.javadoc.build.RuntimeJavadocHelper.elementDocFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.elementNameFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.elementTypeFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.enumConstantsFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.fieldsFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.methodsFieldName;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.paramTypesFieldName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.msuno.javadoc.docs.ClassJavadoc;
import cn.msuno.javadoc.docs.FieldJavadoc;
import cn.msuno.javadoc.docs.MethodJavadoc;

public class JsonJavadocReader {
    
    public static ClassJavadoc readClassJavadoc(String qualifiedClassName, JSONObject json) {
        String className = qualifiedClassName.replace("$", ".");
        List<FieldJavadoc> fields = readFieldDocs(qualifiedClassName, json.getJSONArray(fieldsFieldName()));
        List<FieldJavadoc> enumConstants = readFieldDocs(qualifiedClassName,
                json.getJSONArray(enumConstantsFieldName()));
        List<MethodJavadoc> methods = readMethodDocs(qualifiedClassName, json.getJSONArray(methodsFieldName()));
        String classJavadocString = json.getOrDefault(elementDocFieldName(), "").toString();
        return JavadocParser.parseClassJavadoc(className, classJavadocString, fields, enumConstants, methods);
    }
    
    private static List<FieldJavadoc> readFieldDocs(String owningClass, JSONArray fieldsValue) {
        if (fieldsValue == null) {
            // old versions might not have this JSON field
            return Collections.emptyList();
        }
        List<FieldJavadoc> fields = new ArrayList<>(fieldsValue.size());
        for (int i = 0; i < fieldsValue.size(); i++) {
            fields.add(readFieldDoc(owningClass, fieldsValue.getJSONObject(i)));
        }
        return fields;
    }
    
    private static FieldJavadoc readFieldDoc(String owningClass, JSONObject fieldValue) {
        String fieldName = fieldValue.getOrDefault(elementNameFieldName(), "").toString();
        String fieldDoc = fieldValue.getOrDefault(elementDocFieldName(), "").toString();
        String type = fieldValue.getOrDefault(elementTypeFieldName(), "").toString();
        return JavadocParser.parseFieldJavadoc(owningClass, fieldName, fieldDoc, type);
    }
    
    private static List<MethodJavadoc> readMethodDocs(String owningClass, JSONArray methodsValue) {
        List<MethodJavadoc> methods = new ArrayList<>(methodsValue.size());
        for (int i = 0; i < methodsValue.size(); i++) {
            methods.add(readMethodDoc(owningClass, methodsValue.getJSONObject(i)));
        }
        return methods;
    }
    
    private static MethodJavadoc readMethodDoc(String owningClass, JSONObject methodValue) {
        String methodName = methodValue.getStr(elementNameFieldName());
        List<String> paramTypes = readParamTypes(methodValue.getJSONArray(paramTypesFieldName()));
        String methodDoc = methodValue.getStr(elementDocFieldName());
        return JavadocParser.parseMethodJavadoc(owningClass, methodName, paramTypes, methodDoc);
    }
    
    private static List<String> readParamTypes(JSONArray paramTypesValue) {
        List<String> paramTypes = new ArrayList<>(paramTypesValue.size());
        for (Object v : paramTypesValue) {
            paramTypes.add(v.toString());
        }
        return paramTypes;
    }
}
