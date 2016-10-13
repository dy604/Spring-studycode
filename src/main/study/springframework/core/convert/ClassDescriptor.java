package study.springframework.core.convert;

import study.springframework.core.GenericCollectionTypeResolver;

import java.lang.annotation.Annotation;

/**
 * Created by pc on 2016/10/13.
 */
class ClassDescriptor extends AbstractDescriptor {

    ClassDescriptor(Class<?> type) {
        super(type);
    }

    @Override
    public Annotation[] getAnnotations() {
        return TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Class<?> resolveCollectionElementType() {
        return GenericCollectionTypeResolver.getCollectionType((Class) getType());
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Class<?> resolveMapKeyType() {
        return GenericCollectionTypeResolver.getMapKeyType((Class) getType());
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Class<?> resolveMapValueType() {
        return GenericCollectionTypeResolver.getMapValueType((Class) getType());
    }

    @Override
    protected AbstractDescriptor nested(Class<?> type, int typeIndex) {
        return new ClassDescriptor(type);
    }
}
