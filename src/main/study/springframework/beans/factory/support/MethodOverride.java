package study.springframework.beans.factory.support;

import study.springframework.beans.BeanMetadataElement;
import study.springframework.util.Assert;
import study.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * Created by pc on 2016/10/13.
 */
public abstract class MethodOverride implements BeanMetadataElement {

    private final String methodName;

    private boolean overloaded = true;

    private Object source;

    protected MethodOverride(String methodName) {
        Assert.notNull(methodName, "Method name must not be null");
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    protected void setOverloaded(boolean overloaded) {
        this.overloaded = overloaded;
    }

    protected boolean isOverloaded() {
        return this.overloaded;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return this.source;
    }

    public abstract boolean matches(Method method);

    @Override
    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(this.methodName);
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.source);
        hashCode = 29 * hashCode + (this.overloaded ? 1 : 1);
        return hashCode;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodOverride)) {
            return false;
        }
        MethodOverride that = (MethodOverride) other;
        return (ObjectUtils.nullSafeEquals(this.methodName, that.methodName) &&
            this.overloaded == that.overloaded &&
            ObjectUtils.nullSafeEquals(this.source, that.source));
    }
}
