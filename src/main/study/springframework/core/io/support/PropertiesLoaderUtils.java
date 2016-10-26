package study.springframework.core.io.support;

import study.springframework.core.io.Resource;
import study.springframework.util.Assert;
import study.springframework.util.ClassUtils;
import study.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by dy on 2016/10/26.
 */
public abstract class PropertiesLoaderUtils {

    public static Properties loadProperties(Resource resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource);
        return props;
    }

    public static void fillProperties(Properties props, Resource resource) throws IOException {
        InputStream is = resource.getInputStream();
        try {
            props.load(is);
        }
        finally {
            is.close();
        }
    }

    public static Properties loadAllProperties(String resourceName) throws IOException {
        return loadAllProperties(resourceName, null);
    }

    public static Properties loadAllProperties(String resourceName, ClassLoader classLoader) throws IOException {
        Assert.notNull(resourceName, "Resource name must not be null");
        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = ClassUtils.getDefaultClassLoader();
        }
        Properties properties = new Properties();
        Enumeration urls = clToUse.getResources(resourceName);
        while (urls.hasMoreElements()) {
            URL url = (URL) urls.nextElement();
            InputStream is = null;
            try {
                URLConnection con = url.openConnection();
                ResourceUtils.useCachesIfNecessary(con);
                is = con.getInputStream();
                properties.load(is);
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return properties;
    }
}
