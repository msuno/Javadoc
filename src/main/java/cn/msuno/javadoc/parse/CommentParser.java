package cn.msuno.javadoc.parse;


import static cn.msuno.javadoc.build.RuntimeJavadocHelper.isBlank;
import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.msuno.javadoc.docs.Comment;
import cn.msuno.javadoc.docs.CommentElement;
import cn.msuno.javadoc.docs.CommentText;
import cn.msuno.javadoc.docs.InlineLink;
import cn.msuno.javadoc.docs.InlineTag;
import cn.msuno.javadoc.docs.InlineValue;
import cn.msuno.javadoc.docs.Link;
import cn.msuno.javadoc.docs.Value;

class CommentParser {
    
    private static final Pattern inlineTag = compile("\\{@(\\w+)(?:\\s+([\\w#][^}]+)?)?}");
    
    private static final Pattern valuePattern = compile("^(?:(?<classname>[\\w.]+)#)?#?(?<member>\\w+)$");
    
    static Comment parse(String owningClass, String commentText) {
        return isBlank(commentText) ? Comment.createEmpty() : new Comment(parseElements(owningClass, commentText.trim()));
    }
    
    private static List<CommentElement> parseElements(String owningClass, String commentText) {
        Matcher matcher = inlineTag.matcher(commentText);
        List<CommentElement> elements = new ArrayList<>();
        int pos = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (start > pos) {
                elements.add(new CommentText(commentText.substring(pos, start)));
            }
            CommentElement elt = createTagElement(owningClass, matcher.group(1), matcher.group(2));
            elements.add(elt);
            pos = matcher.end();
        }
        
        if (pos < commentText.length()) {
            elements.add(new CommentText(commentText.substring(pos)));
        }
        return elements;
    }
    
    private static CommentElement createTagElement(String owningClass, String name, String value) {
        if ("link".equals(name)) {
            return createLinkElement(owningClass, value);
        } else if ("value".equals(name)) {
            return createValueElement(owningClass, value);
        } else {
            return new InlineTag(name, value);
        }
    }
    
    private static InlineValue createValueElement(String owningClass, String value) {
        if (value == null || value.trim().isEmpty()) {
            return new InlineValue(new Value(null, null));
        }
        
        Matcher linkMatcher = valuePattern.matcher(value);
        if (!linkMatcher.matches()) {
            throw new AssertionError("Value didn't match regex format");
        }
        String classRef = linkMatcher.group("classname");
        String memberRef = linkMatcher.group("member");
        
        String effectiveClassName = classRef == null ? owningClass : classRef;
        return new InlineValue(new Value(effectiveClassName, memberRef));
    }
    
    private static InlineLink createLinkElement(String owningClass, String value) {
        Link javadocLink = LinkParser.createLinkElement(owningClass, value);
        if (javadocLink == null) {
            throw new AssertionError("Link didn't match regex format");
        }
        return new InlineLink(javadocLink);
    }
}
