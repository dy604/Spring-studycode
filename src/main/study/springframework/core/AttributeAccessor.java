package study.springframework.core;

/**
 * Created by dy on 2016/10/11.
 */
public interface AttributeAccessor {

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    Object removeAttribute(String name);

    boolean hasAttribute(String name);

    String[] attributeNames();
}
