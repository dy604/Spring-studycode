package study.springframework.beans.factory;

/**
 * Created by dy on 2016/10/11.
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();

    boolean isSingleton();
}
