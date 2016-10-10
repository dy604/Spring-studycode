package study.springframework.beans.factory;

import study.springframework.beans.BeansException;

/**
 * Created by dy on 2016/10/10.
 */
public interface ObjectFactory {

    T getObject() throws BeansException;
}
