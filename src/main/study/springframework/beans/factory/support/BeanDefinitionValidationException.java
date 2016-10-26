package study.springframework.beans.factory.support;

import study.springframework.beans.FatalBeanException;

/**
 * Created by pc on 2016/10/13.
 */
public class BeanDefinitionValidationException extends FatalBeanException {

    public BeanDefinitionValidationException(String msg) {
        super(msg);
    }

    public BeanDefinitionValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
