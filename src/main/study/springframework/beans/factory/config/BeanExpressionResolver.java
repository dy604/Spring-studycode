package study.springframework.beans.factory.config;

import study.springframework.beans.BeansException;

/**
 * Created by dy on 2016/10/11.
 */
public interface BeanExpressionResolver {

    Object evaluate(String value, BeanExpressionContext evalContext) throws BeansException;
}
