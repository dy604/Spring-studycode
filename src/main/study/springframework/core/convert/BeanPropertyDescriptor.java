package study.springframework.core.convert;

import study.springframework.core.GenericCollectionTypeResolver;
import study.springframework.core.MethodParameter;

import java.lang.annotation.Annotation;

/**
 * Created by pc on 2016/10/13.
 */
class BeanPropertyDescriptor extends AbstractDescriptor {

    private final Property property;

    private final MethodParameter methodParameter;

    private final Annotation[] annotations;

    public BeanPropertyDescriptor(Property property) {
        super(property.getType());
        this.property = property;
        this.methodParameter = property.getMethodParameter();
        this.annotations = property.getAnnotations();
    }


    @Override
    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    @Override
    protected Class<?> resolveCollectionElementType() {
        return GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
    }

    @Override
    protected Class<?> resolveMapKeyType() {
        return GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
    }

    @Override
    protected Class<?> resolveMapValueType() {
        return GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
    }

    @Override
    protected AbstractDescriptor nested(Class<?> type, int typeIndex) {
        MethodParameter methodParameter = new MethodParameter(this.methodParameter);
        methodParameter.increaseNestingLevel();
        methodParameter.setTypeIndexForCurrentLevel(typeIndex);
        return new BeanPropertyDescriptor(type, this.property, methodParameter, this.annotations);
    }


    // internal

    private BeanPropertyDescriptor(Class<?> type, Property propertyDescriptor, MethodParameter methodParameter, Annotation[] annotations) {
        super(type);
        this.property = propertyDescriptor;
        this.methodParameter = methodParameter;
        this.annotations = annotations;
    }
}
