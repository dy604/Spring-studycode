package study.springframework.beans.factory.support;

import study.springframework.beans.MutablePropertyValues;
import study.springframework.beans.factory.config.BeanDefinition;
import study.springframework.beans.factory.config.BeanDefinitionHolder;
import study.springframework.beans.factory.config.ConstructorArgumentValues;
import study.springframework.util.Assert;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pc on 2016/10/13.
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    private final Map<Member, Boolean> externallyManagedConfigMembers = new ConcurrentHashMap<Member, Boolean>(6);

    private final Map<String, Boolean> externallyManagedInitMethods = new ConcurrentHashMap<String, Boolean>(0);

    private final Map<String, Boolean> externallyManagedDestroyMethods = new ConcurrentHashMap<String, Boolean>(0);

    private BeanDefinitionHolder decoratedDefinition;

    boolean allowCaching = true;

    boolean isFactoryMethodUnique = false;

    Object resolvedConstructorOrFactoryMethod;

    boolean constructorArgumentsResolved = false;

    Object[] resolvedConstructorArguments;

    Object[] preparedConstructorArguments;

    final Object constructorArgumentLock = new Object();

    volatile boolean beforeInstantiationResolved;

    boolean postProcessed = false;

    final Object postProcessingLock = new Object();

    public RootBeanDefinition() {
        super();
    }

    public RootBeanDefinition(Class beanClass) {
        super();
        setBeanClass(beanClass);
    }

    @Deprecated
    public RootBeanDefinition(Class beanClass, boolean singleton) {
        super();
        setBeanClass(beanClass);
        setSingleton(singleton);
    }

    @Deprecated
    public RootBeanDefinition(Class beanClass, int autowireMode) {
        super();
        setBeanClass(beanClass);
        setAutowireMode(autowireMode);
    }

    public RootBeanDefinition(Class beanClass, int autowireMode, boolean dependencyCheck) {
        super();
        setBeanClass(beanClass);
        setAutowireMode(autowireMode);
        if (dependencyCheck && getResolvedAutowireMode() != AUTOWIRE_CONSTRUCTOR) {
            setDependencyCheck(RootBeanDefinition.DEPENDENCY_CHECK_OBJECTS);
        }
    }

    @Deprecated
    public RootBeanDefinition(Class beanClass, MutablePropertyValues pvs) {
        super(null, pvs);
        setBeanClass(beanClass);
    }

    @Deprecated
    public RootBeanDefinition(Class beanClass, MutablePropertyValues pvs, boolean singleton) {
        super(null, pvs);
        setBeanClass(beanClass);
        setSingleton(singleton);
    }

    public RootBeanDefinition(Class beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
        super(cargs, pvs);
        setBeanClass(beanClass);
    }

    public RootBeanDefinition(String beanClassName) {
        setBeanClassName(beanClassName);
    }

    public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
        super(cargs, pvs);
        setBeanClassName(beanClassName);
    }

    public RootBeanDefinition(RootBeanDefinition original) {
        this((BeanDefinition) original);
    }

    RootBeanDefinition(BeanDefinition original) {
        super(original);
        if (original instanceof RootBeanDefinition) {
            RootBeanDefinition originalRbd = (RootBeanDefinition) original;
            this.decoratedDefinition = originalRbd.decoratedDefinition;
            this.isFactoryMethodUnique = originalRbd.isFactoryMethodUnique;
        }
    }


    public String getParentName() {
        return null;
    }

    public void setParentName(String parentName) {
        if (parentName != null) {
            throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
        }
    }

    @Override
    public void setLazyInit() {

    }

    /**
     * Specify a factory method name that refers to a non-overloaded method.
     */
    public void setUniqueFactoryMethodName(String name) {
        Assert.hasText(name, "Factory method name must not be empty");
        setFactoryMethodName(name);
        this.isFactoryMethodUnique = true;
    }

    /**
     * Check whether the given candidate qualifies as a factory method.
     */
    public boolean isFactoryMethod(Method candidate) {
        return (candidate != null && candidate.getName().equals(getFactoryMethodName()));
    }

    /**
     * Return the resolved factory method as a Java Method object, if available.
     * @return the factory method, or <code>null</code> if not found or not resolved yet
     */
    public Method getResolvedFactoryMethod() {
        synchronized (this.constructorArgumentLock) {
            Object candidate = this.resolvedConstructorOrFactoryMethod;
            return (candidate instanceof Method ? (Method) candidate : null);
        }
    }


    public void registerExternallyManagedConfigMember(Member configMember) {
        this.externallyManagedConfigMembers.put(configMember, Boolean.TRUE);
    }

    public boolean isExternallyManagedConfigMember(Member configMember) {
        return this.externallyManagedConfigMembers.containsKey(configMember);
    }

    public void registerExternallyManagedInitMethod(String initMethod) {
        this.externallyManagedInitMethods.put(initMethod, Boolean.TRUE);
    }

    public boolean isExternallyManagedInitMethod(String initMethod) {
        return this.externallyManagedInitMethods.containsKey(initMethod);
    }

    public void registerExternallyManagedDestroyMethod(String destroyMethod) {
        this.externallyManagedDestroyMethods.put(destroyMethod, Boolean.TRUE);
    }

    public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
        return this.externallyManagedDestroyMethods.containsKey(destroyMethod);
    }

    public void setDecoratedDefinition(BeanDefinitionHolder decoratedDefinition) {
        this.decoratedDefinition = decoratedDefinition;
    }

    public BeanDefinitionHolder getDecoratedDefinition() {
        return this.decoratedDefinition;
    }


    @Override
    public RootBeanDefinition cloneBeanDefinition() {
        return new RootBeanDefinition(this);
    }

    @Override
    public boolean equals(Object other) {
        return (this == other || (other instanceof RootBeanDefinition && super.equals(other)));
    }

    @Override
    public String toString() {
        return "Root bean: " + super.toString();
    }
}