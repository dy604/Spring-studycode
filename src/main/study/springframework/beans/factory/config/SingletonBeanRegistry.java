package study.springframework.beans.factory.config;

/**
 * Created by dy on 2016/10/10.
 */
public interface SingletonBeanRegistry {

    void registrySingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

    int getSingletonCount();
}
