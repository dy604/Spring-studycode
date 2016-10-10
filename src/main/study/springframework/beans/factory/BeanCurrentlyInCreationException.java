package study.springframework.beans.factory;

import study.springframework.beans.BeanCreationException;

/**
 * Created by dy on 2016/10/10.
 */
public class BeanCurrentlyInCreationException extends BeanCreationException {

    public BeanCurrentlyInCreationException(String beanName) {
        super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
    }

    public BeanCurrentlyInCreationException(String beanName, String msg) {
        super(beanName, msg);
    }
}
