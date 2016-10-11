package study.springframework.beans.factory.support;

import study.springframework.beans.factory.NoSuchBeanDefinitionException;
import study.springframework.beans.factory.config.BeanDefinition;
import study.springframework.core.AliasRegistry;

/**
 * Created by dy on 2016/10/11.
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    boolean isBeanNameInUse(String beanName);
}
