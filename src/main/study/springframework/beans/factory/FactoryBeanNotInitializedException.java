package study.springframework.beans.factory;

import study.springframework.beans.FatalBeanException;

/**
 * Created by dy on 2016/10/11.
 */
public class FactoryBeanNotInitializedException extends FatalBeanException {

    public FactoryBeanNotInitializedException() {
        super("FactoryBean is not fully initialized yet");
    }

    public FactoryBeanNotInitializedException(String msg) {
        super(msg);
    }
}
