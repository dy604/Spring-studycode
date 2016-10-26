package study.springframework.beans.factory;

import study.springframework.beans.BeansException;

/**
 * Created by pc on 2016/10/13.
 */
public class BeanNotOfRequiredTypeException extends BeansException {

    private String beanName;

    private Class requiredType;

    private Class actualType;

    public BeanNotOfRequiredTypeException(String beanName, Class requiredType, Class actualType) {
        super("Bean named '" + beanName + "' must be of type [" + requiredType.getName() +
                "], but was actually of type [" + actualType.getName() + "]");
        this.beanName = beanName;
        this.requiredType = requiredType;
        this.actualType = actualType;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public Class getRequiredType() {
        return this.requiredType;
    }

    public Class getActualType() {
        return this.actualType;
    }
}
