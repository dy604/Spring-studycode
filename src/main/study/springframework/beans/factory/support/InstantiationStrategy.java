package study.springframework.beans.factory.support;

import study.springframework.beans.BeansException;
import study.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by dy on 2016/10/26.
 */
public interface InstantiationStrategy {

    Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner)
            throws BeansException;

    Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner,
            Constructor<?> ctor, Object[] args) throws BeansException;

    Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner,
            Object factoryBean, Method factoryMethod, Object[] args) throws BeansException;

}
