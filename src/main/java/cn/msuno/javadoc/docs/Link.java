package cn.msuno.javadoc.docs;


import static cn.msuno.javadoc.build.RuntimeJavadocHelper.join;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.unmodifiableDefensiveCopy;

import java.util.List;
import java.util.Objects;

public class Link {
    private final String label;
    private final String referencedClassName;
    private final String referencedMemberName;
    private final List<String> params;
    
    public Link(String label, String referencedClassName, String referencedMemberName, List<String> params) {
        this.label = label;
        this.referencedClassName = referencedClassName;
        this.referencedMemberName = referencedMemberName;
        this.params = unmodifiableDefensiveCopy(params);
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getReferencedClassName() {
        return referencedClassName;
    }
    
    public String getReferencedMemberName() {
        return referencedMemberName;
    }
    
    public List<String> getParams() {
        return params;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(label, link.label) &&
                Objects.equals(referencedClassName, link.referencedClassName)
                && Objects.equals(referencedMemberName, link.referencedMemberName)
                && Objects.equals(params, link.params);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(label, referencedClassName, referencedMemberName, params);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if (referencedClassName != null) {
            sb.append(referencedClassName);
        }
        
        if (referencedMemberName != null) {
            sb.append('#').append(referencedMemberName);
            
            if (!params.isEmpty()) {
                sb.append('(').append(join(", ", params)).append(")");
            }
        }
        
        if (label != null) {
            sb.append(' ').append(label);
        }
        
        return sb.toString();
    }
}
