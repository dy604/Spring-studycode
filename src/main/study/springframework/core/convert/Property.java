package study.springframework.core.convert;

import study.springframework.core.GenericCollectionTypeResolver;
import study.springframework.core.GenericTypeResolver;
import study.springframework.core.MethodParameter;
import study.springframework.util.ObjectUtils;
import study.springframework.util.ReflectionUtils;
import study.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pc on 2016/10/13.
 */
public final class Property {

    private static Map<Property, Annotation[]> annotationCache = new ConcurrentHashMap<Property, Annotation[]>();

    private final Class<?> objectType;

    private final Method readMethod;

    private final Method writeMethod;

    private final String name;

    private final MethodParameter methodParameter;

    private Annotation[] annotations;

    public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name, MethodParameter methodParameter) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.name = (name == null ? resolveName() : name);
        this.methodParameter = methodParameter;
    }

    public Class<?> getObjectType() {
        return this.objectType;
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public Class<?> getType() {
        return this.methodParameter.getParameterType();
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    public String getName() {
        return this.name;
    }

    Annotation[] getAnnotations() {
        if (this.annotations == null) {
            this.annotations = resolveAnnotations();
        }
        return this.annotations;
    }

    private String resolveName() {
        if (this.readMethod != null) {
            int index = this.readMethod.getName().indexOf("get");
            if (index != -1) {
                index += 3;
            } else {
                index = this.readMethod.getName().indexOf("is");
                if (index == -1) {
                    throw new IllegalArgumentException("not a getter method");
                }
                index += 2;
            }
            return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
        } else {
            int index = this.writeMethod.getName().indexOf("set") + 3;
            if (index == -1) {
                throw new IllegalArgumentException("Not a setter method");
            }
            return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
        }
    }

    private MethodParameter resolveMethodParameter() {
        MethodParameter read = resolveReadMethodParameter();
        MethodParameter write = resolveWriteMethodParameter();
        if (write == null) {
            if (read == null) {
                throw new IllegalArgumentException("Property is neither readable nor writeable");
            }
            return read;
        }
        if (read != null) {
            Class<?> readType = read.getParameterType();
            Class<?> writeType = write.getParameterType();
            if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
                return read;
            }
        }
        return write;
    }

    private MethodParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getReadMethod(), -1));
    }

    private MethodParameter resolveParameterType(MethodParameter parameter) {
        GenericTypeResolver.resolveParameterType(parameter, getObjectType());
        return parameter;
    }

    private Annotation[] resolveAnnotations() {
        Annotation[] annotations = annotationCache.get(this);
        if(annotations == null) {
            Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<Class<? extends Annotation>, Annotation>();
            addAnnotationsToMap(annotationMap, getReadMethod());
            addAnnotationsToMap(annotationMap, getWriteMethod());
            addAnnotationsToMap(annotationMap, getField());
            annotations = annotationMap.values().toArray(new Annotation[annotationMap.size()]);
            annotationCache.put(this, annotations);
        }
        return annotations;
    }

    private void addAnnotationsToMap(
            Map<Class<? extends Annotation>, Annotation> annotationMap,
            AnnotatedElement object) {
        if (object != null) {
            for (Annotation annotation : object.getAnnotations()) {
                annotationMap.put(annotation.annotationType(), annotation);
            }
        }
    }

    private Field getField() {
        String name = getName();
        if (!StringUtils.hasLength(name)) {
            return null;
        }
        Class<?> declaringClass = declaringClass();
        Field field = ReflectionUtils.findField(declaringClass, name);
        if (field == null) {
            // Same lenient fallback checking as in CachedIntrospectionResults...
            field = ReflectionUtils.findField(declaringClass,
                    name.substring(0, 1).toLowerCase() + name.substring(1));
            if (field == null) {
                field = ReflectionUtils.findField(declaringClass,
                        name.substring(0, 1).toUpperCase() + name.substring(1));
            }
        }
        return field;
    }

    private Class<?> declaringClass() {
        if (getReadMethod() != null) {
            return getReadMethod().getDeclaringClass();
        }
        else {
            return getWriteMethod().getDeclaringClass();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(objectType);
        hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(readMethod);
        hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(writeMethod);
        hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(name);
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Property other = (Property) obj;
        boolean equals = true;
        equals &= ObjectUtils.nullSafeEquals(objectType, other.objectType);
        equals &= ObjectUtils.nullSafeEquals(readMethod, other.readMethod);
        equals &= ObjectUtils.nullSafeEquals(writeMethod, other.writeMethod);
        equals &= ObjectUtils.nullSafeEquals(name, other.name);
        return equals;
    }
}

