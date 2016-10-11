package study.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by dy on 2016/10/11.
 */
public interface ParameterNameDiscoverer {

    String[] getParameterNames(Method method);

    String[] getParameterNames(Constructor<?> ctor);
}
