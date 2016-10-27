package study.springframework.cglib.core;

import org.objectweb.asm.Type;

/**
 * Created by dy on 2016/10/27.
 */
public abstract class ClassInfo {

    protected ClassInfo() {
    }

    abstract public Type getType();
    abstract public Type getSuperType();
    abstract public Type[] getInterfaces();
    abstract public int getModifiers();

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof ClassInfo))
            return false;
        return getType().equals(((ClassInfo)o).getType());
    }

    public int hashCode() {
        return getType().hashCode();
    }

    public String toString() {
        // TODO: include modifiers, superType, interfaces
        return getType().getClassName();
    }
}
