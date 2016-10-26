package study.springframework.beans.factory.config;

import study.springframework.beans.BeansException;

/**
 * Created by pc on 2016/10/13.
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
