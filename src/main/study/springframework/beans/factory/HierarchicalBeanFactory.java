package study.springframework.beans.factory;

/**
 * Created by dy on 2016/10/11.
 */
public interface HierarchicalBeanFactory extends BeanFactory {

    BeanFactory getParentBeanFactory();

    boolean containsLocalBean(String name);
}
