package study.springframework.beans;

import org.apache.commons.logging.LogFactory;
import study.springframework.core.GenericTypeResolver;
import study.springframework.core.MethodParameter;
import study.springframework.util.ClassUtils;
import study.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dy on 2016/10/26.
 */
class GenericTypeAwarePropertyDescriptor extends PropertyDescriptor {

    private final Class beanClass;
    private final Method readMethod;
    private final Method writeMethod;
    private final Class propertyEditorClass;
    private volatile Set<Method> ambiguousWriteMethods;
    private Class propertyType;
    private MethodParameter writeMethodParameter;

    public GenericTypeAwarePropertyDescriptor(Class beanClass, String propertyName,
            Method readMethod, Method writeMethod, Class propertyEditorClass)
            throws IntrospectionException {
        super(propertyName, null, null);
        this.beanClass = beanClass;
        this.propertyEditorClass = propertyEditorClass;

        Method readMethodToUse = BridgeMethodResolver.findBridgeMethod(readMethod);
        Method writeMethodToUse = BridgeMethodResolver.findBridgeMethod(writeMethod);
        if (writeMethodToUse == null && readMethodToUse != null) {
            writeMethodToUse = ClassUtils.getMethodIfAvailable(this.beanClass,
                    "set" + StringUtils.capitalize(getName()), readMethodToUse.getReturnType());
        }
        this.readMethod = readMethodToUse;
        this.writeMethod = writeMethodToUse;

        if (this.writeMethod != null && this.readMethod == null) {
            Set<Method> ambiguousCandidates = new HashSet<Method>();
            for (Method method : beanClass.getMethods()) {
                if (method.getName().equals(writeMethodToUse.getName()) &&
                        !method.equals(writeMethodToUse) && !method.isBridge()) {
                    ambiguousCandidates.add(method);
                }
            }
            if (!ambiguousCandidates.isEmpty()) {
                this.ambiguousWriteMethods = ambiguousCandidates;
            }
        }
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Method getReadMethod() {
        return this.readMethod;
    }

    @Override
    public Method getWriteMethod() {
        return this.writeMethod;
    }

    public Method getWriteMethodForActualAccess() {
        Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
        if (ambiguousCandidates != null) {
            this.ambiguousWriteMethods = null;
            LogFactory.getLog(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '" +
                    getName() + "' being accessed! Ambiguous write methods found next to actually used [" +
                    this.writeMethod + "]: " + ambiguousCandidates);
        }
        return this.writeMethod;
    }

    @Override
    public Class getPropertyEditorClass() {
        return this.propertyEditorClass;
    }

    @Override
    public synchronized Class getPropertyType() {
        if (this.propertyType == null) {
            if (this.readMethod != null) {
                this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
            }
            else {
                MethodParameter writeMethodParam = getWriteMethodParameter();
                if (writeMethodParam != null) {
                    this.propertyType = writeMethodParam.getParameterType();
                }
                else {
                    this.propertyType = super.getPropertyType();
                }
            }
        }
        return this.propertyType;
    }

    public synchronized MethodParameter getWriteMethodParameter() {
        if (this.writeMethod == null) {
            return null;
        }
        if (this.writeMethodParameter == null) {
            this.writeMethodParameter = new MethodParameter(this.writeMethod, 0);
            GenericTypeResolver.resolveParameterType(this.writeMethodParameter, this.beanClass);
        }
        return this.writeMethodParameter;
    }
}
