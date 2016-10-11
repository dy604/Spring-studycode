package study.springframework.beans.factory.config;

import study.springframework.beans.factory.BeanFactory;
import study.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * Created by dy on 2016/10/11.
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

    void setBeanClassLoader(ClassLoader beanClassLoader);

    ClassLoader getBeanClassLoader();

    void setTempClassLoader(ClassLoader tempClassLoader);

    ClassLoader getTempClassLoader();

    void setCacheBeanMetadata(boolean cacheBeanMetadata);

    boolean isCacheBeanMetadata();

    void setBeanExpressionResolver(BeanExpressionResolver resolver);
}
