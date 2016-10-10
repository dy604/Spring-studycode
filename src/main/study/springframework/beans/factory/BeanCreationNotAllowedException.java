package study.springframework.beans.factory;

import study.springframework.beans.BeanCreationException;

/**
 * Created by dy on 2016/10/10.
 */
public class BeanCreationNotAllowedException extends BeanCreationException {

    public BeanCreationNotAllowedException(String beanName, String msg) {
        super(beanName, msg);
    }
}
