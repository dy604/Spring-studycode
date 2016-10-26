package study.springframework.beans.factory.support;

import study.springframework.beans.BeanInstantiationException;
import study.springframework.beans.BeanUtils;
import study.springframework.beans.BeansException;
import study.springframework.beans.factory.BeanDefinitionStoreException;
import study.springframework.beans.factory.BeanFactory;
import study.springframework.util.ReflectionUtils;
import study.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 * Created by dy on 2016/10/26.
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal<Method>();

    public static Method getCurrentlyInvokedFactoryMethod() {
        return currentlyInvokedFactoryMethod.get();
    }

    @Override
    public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) throws BeansException {
        if (beanDefinition.getMethodOverrides().isEmpty()) {
            Constructor<?> constructorToUse;
            synchronized (beanDefinition.constructorArgumentLock) {
                constructorToUse = (Constructor<?>) beanDefinition.resolvedConstructorOrFactoryMethod;
                if (constructorToUse == null) {
                    final Class clazz = beanDefinition.getBeanClass();
                    if (clazz.isInterface()) {
                        throw new BeanInstantiationException(clazz, "Specified class is an interface");
                    }
                    try {
                        if (System.getSecurityManager() != null) {
                            constructorToUse = AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor>() {
                                @Override
                                public Constructor run() throws Exception {
                                    return clazz.getDeclaredConstructor((Class[]) null);
                                }
                            });
                        } else {
                            constructorToUse = clazz.getDeclaredConstructor((Class[]) null);
                        }
                        beanDefinition.resolvedConstructorOrFactoryMethod = constructorToUse;
                    } catch (Exception e) {
                        throw new BeanInstantiationException(clazz, "No default constructor found");
                    }
                }
            }
            return BeanUtils.instantiateClass(constructorToUse);
        }
        else {
            return instantiateWithMethodInjection(beanDefinition, beanName, owner);
        }
    }

    protected Object instantiateWithMethodInjection(
            RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) {

        throw new UnsupportedOperationException(
                "Method Injection not supported in SimpleInstantiationStrategy");
    }

    public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner,
                              final Constructor<?> ctor, Object[] args) {

        if (beanDefinition.getMethodOverrides().isEmpty()) {
            if (System.getSecurityManager() != null) {
                // use own privileged to change accessibility (when security is on)
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        ReflectionUtils.makeAccessible(ctor);
                        return null;
                    }
                });
            }
            return BeanUtils.instantiateClass(ctor, args);
        }
        else {
            return instantiateWithMethodInjection(beanDefinition, beanName, owner, ctor, args);
        }
    }

    protected Object instantiateWithMethodInjection(RootBeanDefinition beanDefinition,
                                                    String beanName, BeanFactory owner, Constructor ctor, Object[] args) {

        throw new UnsupportedOperationException(
                "Method Injection not supported in SimpleInstantiationStrategy");
    }

    public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner,
                              Object factoryBean, final Method factoryMethod, Object[] args) {

        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                        ReflectionUtils.makeAccessible(factoryMethod);
                        return null;
                    }
                });
            }
            else {
                ReflectionUtils.makeAccessible(factoryMethod);
            }

            Method priorInvokedFactoryMethod = currentlyInvokedFactoryMethod.get();
            try {
                currentlyInvokedFactoryMethod.set(factoryMethod);
                return factoryMethod.invoke(factoryBean, args);
            }
            finally {
                if (priorInvokedFactoryMethod != null) {
                    currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
                }
                else {
                    currentlyInvokedFactoryMethod.remove();
                }
            }
        }
        catch (IllegalArgumentException ex) {
            throw new BeanDefinitionStoreException(
                    "Illegal arguments to factory method [" + factoryMethod + "]; " +
                            "args: " + StringUtils.arrayToCommaDelimitedString(args));
        }
        catch (IllegalAccessException ex) {
            throw new BeanDefinitionStoreException(
                    "Cannot access factory method [" + factoryMethod + "]; is it public?");
        }
        catch (InvocationTargetException ex) {
            throw new BeanDefinitionStoreException(
                    "Factory method [" + factoryMethod + "] threw exception", ex.getTargetException());
        }
    }
}
