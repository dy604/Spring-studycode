package study.springframework.beans.factory;

/**
 * Created by pc on 2016/10/13.
 */
public interface SmartFactoryBean<T> extends FactoryBean<T> {

    boolean isPrototype();

    boolean isEagerInit();
}
