package study.springframework.beans.factory.config;


import study.springframework.beans.MutablePropertyValues;
import study.springframework.core.AttributeAccessor;
import study.springframework.beans.BeanMetadataElement;

/**
 * Created by dy on 2016/10/11.
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    int ROLE_APPLICATION = 0;

    int ROLE_SUPPORT = 1;

    int ROLE_INFRASTRUCTURE = 2;

    String getParentName();

    void setParentName(String parentName);

    String getBeanClassName();

    void setBeanClassName(String beanClassName);

    String getFactoryBeanName();

    void setFactoryBeanName(String factoryBeanName);

    String getFactoryMethodName();

    void setFactoryMethodName(String factoryMethodName);

    String getScope();

    void setScope(String scope);

    boolean isLazyInit();

    void setLazyInit();

    String[] getDependsOn();

    void setDependsOn(String[] dependsOn);

    boolean isAutowireCandidate();

    void setAutowireCandidate(boolean autowireCandidate);

    boolean isPrimary();

    void setPrimary(boolean primary);

    ConstructorArgumentValues getConstructorArgumentValues();

    MutablePropertyValues getPropertyValues();

    boolean isSingleton();

    boolean isPrototype();

    boolean isAbstract();

    int getRole();

    String getDescription();

    String getResourceDescription();

    BeanDefinition getOriginatingBeanDefinition();
}
