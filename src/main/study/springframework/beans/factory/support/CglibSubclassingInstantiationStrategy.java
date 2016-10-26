package study.springframework.beans.factory.support;

import org.apache.commons.logging.LogFactory;
import study.springframework.beans.factory.BeanFactory;
import sun.rmi.runtime.Log;

import java.lang.reflect.Constructor;

/**
 * Created by dy on 2016/10/26.
 */
public class CglibSubclassingInstantiationStrategy extends SimpleInstantiationStrategy {

    private static final int PASSTHROUGH = 0;
    private static final int LOOKUP_OVERRIDE = 1;
    private static final int METHOD_REPLACER = 2;

    @Override
    protected Object instantiateWithMethodInjection(
            RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) {

        // Must generate CGLIB subclass.
        return new CglibSubclassCreator(beanDefinition, owner).instantiate(null, null);
    }

    @Override
    protected Object instantiateWithMethodInjection(
            RootBeanDefinition beanDefinition, String beanName, BeanFactory owner,
            Constructor ctor, Object[] args) {

        return new CglibSubclassCreator(beanDefinition, owner).instantiate(ctor, args);
    }

    private static class CglibSubclassCreator {
        private static final Log logger = LogFactory.getLog(CglibSubclassCreator.class);
        private final RootBeanDefinition beanDefinition;
        private final BeanFactory owner;

        public CglibSubclassCreator(RootBeanDefinition beanDefinition, BeanFactory owner) {
            this.beanDefinition = beanDefinition;
            this.owner = owner;
        }

        public Object instantiate(Constructor ctor, Object[] args) {
            Enhancer enhancer = new Enhancer();

        }
    }
}
