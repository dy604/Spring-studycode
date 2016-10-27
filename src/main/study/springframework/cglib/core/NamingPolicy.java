package study.springframework.cglib.core;

/**
 * Created by dy on 2016/10/27.
 */
public interface NamingPolicy {

    String getClassName(String prefix, String source, Object key, Predicate names);

    boolean equals(Object o);
}
