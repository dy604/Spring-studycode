package study.springframework.beans.factory.config;

import study.springframework.beans.factory.ObjectFactory;

/**
 * Created by dy on 2016/10/11.
 */
public interface Scope {

    Object get(String name, ObjectFactory<?> objectFactory);

    Object remove(String name);

    void registerDestructionCallback(String name, Runnable callback);

    Object resolveContextualObject(String key);

    String getConversationId();
}
