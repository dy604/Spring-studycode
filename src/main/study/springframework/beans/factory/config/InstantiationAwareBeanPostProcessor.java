package study.springframework.beans.factory.config;

import study.springframework.beans.BeansException;
import study.springframework.beans.PropertyValues;

import java.beans.PropertyDescriptor;

/**
 * Created by pc on 2016/10/13.
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
            throws BeansException;
}
