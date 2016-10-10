package study.springframework.core;

/**
 * Created by dy on 2016/10/10.
 */
public interface AliasRegistry {

    void registryAlias(String name, String alias);

    void removeAlias(String alias);

    boolean isAlias(String beanName);

    String[] getAlias(String name);
}
