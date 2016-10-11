package study.springframework.beans.factory.xml;

import study.springframework.beans.BeansException;
import study.springframework.beans.factory.BeanFactory;
import study.springframework.core.io.Resource;

/**
 * Created by dy on 2016/10/10.
 */
@Deprecated
public class XmlBeanFactory extends DefaultListableBeanFactory {

    private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);

    public XmlBeanFactory(Resource resource) throws BeansException {
        this(resource, null);
    }

    public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
        super(parentBeanFactory);
        this.reader.loadBeanDefinitions(resource);
    }
}
