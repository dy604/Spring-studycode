package study.springframework.beans.factory.support;

import study.springframework.beans.factory.config.AutowireCapableBeanFactory;
import study.springframework.core.ParameterNameDiscoverer;

/**
 * Created by dy on 2016/10/26.
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTa
}
