package study.springframework.beans.factory.support;

import study.springframework.beans.BeanMetadataAttributeAccessor;
import study.springframework.util.Assert;

/**
 * Created by pc on 2016/10/13.
 */
public class AutowireCandidateQualifier extends BeanMetadataAttributeAccessor {

    private static String VALUE_KEY = "value";

    private final String typeName;

    public AutowireCandidateQualifier(Class type) {
        this(type.getName());
    }

    public AutowireCandidateQualifier(String typename) {
        Assert.notNull(typename, "Type Name must not be null");
        this.typeName = typename;
    }

    public AutowireCandidateQualifier(Class type, Object value) {
        this(type.getName(), value);
    }

    public AutowireCandidateQualifier(String typeName, Object value) {
        Assert.notNull(typeName, "Type name must not be null");
        this.typeName = typeName;
        setAttribute(VALUE_KEY, value);
    }

    public String getTypeName() {
        return this.typeName;
    }
}
