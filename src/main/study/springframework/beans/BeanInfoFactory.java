package study.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/**
 * Created by dy on 2016/10/26.
 */
public interface BeanInfoFactory {

    BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;
}
