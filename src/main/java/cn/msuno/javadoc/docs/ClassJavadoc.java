package cn.msuno.javadoc.docs;


import static cn.msuno.javadoc.build.RuntimeJavadocHelper.unmodifiableDefensiveCopy;

import java.util.List;

public class ClassJavadoc extends BaseJavadoc {
    
    private final List<FieldJavadoc> fields;
    private final List<FieldJavadoc> enumConstants;
    private final List<MethodJavadoc> methods;
    
    public ClassJavadoc(String name, Comment comment, List<FieldJavadoc> fields, List<FieldJavadoc> enumConstants,
            List<MethodJavadoc> methods, List<OtherJavadoc> other, List<SeeAlsoJavadoc> seeAlso) {
        super(name, comment, seeAlso, other);
        this.fields = unmodifiableDefensiveCopy(fields);
        this.enumConstants = unmodifiableDefensiveCopy(enumConstants);
        this.methods = unmodifiableDefensiveCopy(methods);
    }
    
    public static ClassJavadoc createEmpty(String qualifiedClassName) {
        return new ClassJavadoc(qualifiedClassName, null, null, null, null, null, null) {
            @Override
            public boolean isEmpty() {
                return true;
            }
        };
    }
    
    public List<FieldJavadoc> getFields() {
        return fields;
    }
    
    public List<FieldJavadoc> getEnumConstants() {
        return enumConstants;
    }
    
    public List<MethodJavadoc> getMethods() {
        return methods;
    }
    
    @Override
    public String toString() {
        return "ClassJavadoc{" +
                "name='" + getName() + '\'' +
                ", comment=" + getComment() +
                ", fields=" + fields +
                ", methods=" + methods +
                ", seeAlso=" + getSeeAlso() +
                ", other=" + getOther() +
                '}';
    }
}
