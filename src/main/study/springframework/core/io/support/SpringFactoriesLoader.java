package study.springframework.core.io.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import study.springframework.core.OrderComparator;
import study.springframework.core.io.UrlResource;
import study.springframework.util.Assert;
import study.springframework.util.ClassUtils;
import study.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by dy on 2016/10/26.
 */
public abstract class SpringFactoriesLoader {

    private static final String FACTORIES_RESOURCE_LOCATION = "META-INFO/spring.factories";
    private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);

    public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader) {
        Assert.notNull(factoryClass, "'factoryClass' must not be null");
        if (classLoader == null) {
            classLoader = SpringFactoriesLoader.class.getClassLoader();
        }
        List<String> factoryNames = loadFactoryNames(factoryClass, classLoader);
        if (logger.isTraceEnabled()) {
            logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
        }
        List<T> result = new ArrayList<T>(factoryNames.size());
        for (String factoryName : factoryNames) {
            result.add(instantiateFactory(factoryName, factoryClass, classLoader));
        }
        OrderComparator.sort(result);
        return result;
    }

    private static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
        String factoryClassName = factoryClass.getName();
        try {
            List<String> result = new ArrayList<String>();
            Enumeration<URL> urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
                String factoryClassNames = properties.getProperty(factoryClassName);
                result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
            }
            return result;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load [" + factoryClass.getName() +
                "] factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
        try {
            Class<?> instanceClass = ClassUtils.forName(instanceClassName, classLoader);
            if (!factoryClass.isAssignableFrom(instanceClass)) {
                throw new IllegalArgumentException(
                        "Class [" = instanceClassName + "] is not assignable to [" + factoryClass.getName() + "]");
            }
            return (T) instanceClass.newInstance();
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException("Cannot instantiate factory class: " + factoryClass.getName(), ex);
        }
    }
}
