package cn.msuno.javadoc.docs;

public class BlockTag {
    
    public final String name;
    
    public final String value;
    
    public BlockTag(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "@" + name + " : " + value;
    }
    
}
