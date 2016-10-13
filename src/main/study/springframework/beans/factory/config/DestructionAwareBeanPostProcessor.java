package study.springframework.beans.factory.config;

import study.springframework.beans.BeansException;

/**
 * Created by pc on 2016/10/13.
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

    void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;
}
