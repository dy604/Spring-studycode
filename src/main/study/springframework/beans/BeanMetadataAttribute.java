package study.springframework.beans;

import javafx.beans.binding.ObjectExpression;
import study.springframework.util.Assert;
import study.springframework.util.ObjectUtils;

/**
 * Created by dy on 2016/10/11.
 */
public class BeanMetadataAttribute implements BeanMetadataElement {

    private final String name;

    private final Object value;

    private Object source;

    public BeanMetadataAttribute(String name, Object value) {
        Assert.notNull(name, "Name must not be null");
        this.name = name;
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public Object getSource() {
        return this.source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeanMetadataAttribute)) {
            return false;
        }
        BeanMetadataAttribute otherMa = (BeanMetadataAttribute) other;
        return (this.name.equals(otherMa.name) &&
                ObjectUtils.nullSafeEquals(this.value, otherMa.value) &&
                ObjectUtils.nullSafeEquals(this.source, otherMa.source));
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
    }

    @Override
    public String toString() {
        return "metadata attribute '" + this.name + "'";
    }
}
