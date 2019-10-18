package cn.msuno.javadoc.docs;


import java.util.List;

public class FieldJavadoc extends BaseJavadoc {
    
    private final String type;
    
    public FieldJavadoc(String name, Comment comment, String type, List<OtherJavadoc> other, List<SeeAlsoJavadoc> seeAlso) {
        super(name, comment, seeAlso, other);
        this.type = type;
    }
    
    public static FieldJavadoc createEmpty(String fieldName) {
        return new FieldJavadoc(fieldName, null, null, null,  null) {
            @Override
            public boolean isEmpty() {
                return true;
            }
        };
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "FieldJavadoc{"
                + "name='" + getName() + '\''
                + ", comment=" + getComment()
                + ", other=" + getOther()
                + ", seeAlso=" + getSeeAlso()
                + ", type=" + getType()
                + '}';
    }
}
