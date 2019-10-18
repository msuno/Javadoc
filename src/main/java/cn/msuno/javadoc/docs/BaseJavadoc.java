package cn.msuno.javadoc.docs;

import static cn.msuno.javadoc.build.RuntimeJavadocHelper.nullToEmpty;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.requireNonNull;
import static cn.msuno.javadoc.build.RuntimeJavadocHelper.unmodifiableDefensiveCopy;

import java.util.List;

public abstract class BaseJavadoc {
    
    private final String name;
    private final Comment comment;
    private final List<SeeAlsoJavadoc> seeAlso;
    private final List<OtherJavadoc> other;
    
    BaseJavadoc(String name, Comment comment, List<SeeAlsoJavadoc> seeAlso, List<OtherJavadoc> other) {
        this.name = requireNonNull(name);
        this.comment = Comment.nullToEmpty(comment);
        this.other = unmodifiableDefensiveCopy(other);
        this.seeAlso = unmodifiableDefensiveCopy(seeAlso);
    }
    
    public String getName() {
        return name;
    }
    
    public Comment getComment() {
        return comment;
    }
    
    public List<SeeAlsoJavadoc> getSeeAlso() {
        return seeAlso;
    }
    
    public List<OtherJavadoc> getOther() {
        return other;
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    @Deprecated
    public boolean isPresent() {
        return !isEmpty();
    }
}
