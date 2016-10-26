package study.springframework.beans.factory.support;

import study.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Created by dy on 2016/10/26.
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
}
