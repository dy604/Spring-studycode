package study.springframework.beans;

/**
 * Created by dy on 2016/10/26.
 */
public class BeanInstantiationException extends FatalBeanException {

    private Class beanClass;

    public BeanInstantiationException(Class beanClass, String msg) {
        this(beanClass, msg, null);
    }

    public BeanInstantiationException(Class beanClass, String msg, Throwable cause) {
        super("Could not instantiate bean class [" + beanClass.getName() + "]: " + msg, cause);
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }
}
