package study.springframework.core.convert;

import study.springframework.core.MethodParameter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dy on 2016/10/11.
 */
public class TypeDescriptor {

    static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    private static final Map<Class<?>, TypeDescriptor> typeDescriptorCache = new HashMap<Class<?>, TypeDescriptor>();

    static {
        typeDescriptorCache.put(boolean.class, new TypeDescriptor(boolean.class));
        typeDescriptorCache.put(Boolean.class, new TypeDescriptor(Boolean.class));
        typeDescriptorCache.put(byte.class, new TypeDescriptor(byte.class));
        typeDescriptorCache.put(Byte.class, new TypeDescriptor(Byte.class));
        typeDescriptorCache.put(char.class, new TypeDescriptor(char.class));
        typeDescriptorCache.put(Character.class, new TypeDescriptor(Character.class));
        typeDescriptorCache.put(short.class, new TypeDescriptor(short.class));
        typeDescriptorCache.put(Short.class, new TypeDescriptor(Short.class));
        typeDescriptorCache.put(int.class, new TypeDescriptor(int.class));
        typeDescriptorCache.put(Integer.class, new TypeDescriptor(Integer.class));
        typeDescriptorCache.put(long.class, new TypeDescriptor(long.class));
        typeDescriptorCache.put(Long.class, new TypeDescriptor(Long.class));
        typeDescriptorCache.put(float.class, new TypeDescriptor(float.class));
        typeDescriptorCache.put(Float.class, new TypeDescriptor(Float.class));
        typeDescriptorCache.put(double.class, new TypeDescriptor(double.class));
        typeDescriptorCache.put(Double.class, new TypeDescriptor(Double.class));
        typeDescriptorCache.put(String.class, new TypeDescriptor(String.class));
    }

    private final Class<?> type;

    private final TypeDescriptor elementTypeDescriptor;

    private final TypeDescriptor mapKeyTypeDescriptor;

    private final TypeDescriptor mapValueTypeDescriptor;

    private final Annotation[] annotations;

    public TypeDescriptor(MethodParameter methodParameter) {
        this(new ParameterDescriptor(methodParameter));
    }
}
