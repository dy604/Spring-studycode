package study.springframework.beans.factory;

import study.springframework.beans.BeansException;
import study.springframework.util.Assert;
import study.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by pc on 2016/10/13.
 */
public abstract class BeanFactoryUtils {

    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    public static boolean isFactoryDereference(String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }

    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String beanName = name;
        while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
        }
        return beanName;
    }

    public static boolean isGeneratedBeanName(String name) {
        return (name != null && name.contains(GENERATED_BEAN_NAME_SEPARATOR));
    }

    public static String originalBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        int separatorIndex = name.indexOf(GENERATED_BEAN_NAME_SEPARATOR);
        return (separatorIndex != -1 ? name.substring(0, separatorIndex) : name);
    }

    public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesIncludingAncestors(lbf).length;
    }

    public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesForTypeIncludingAncestors(lbf, Object.class);
    }

    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                List<String> resultList = new ArrayList<String>();
                resultList.addAll(Arrays.asList(result));
                for (String beanName : parentResult) {
                    if (!resultList.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                        resultList.add(beanName);
                    }
                }
                result = StringUtils.toStringArray(resultList);
            }
        }
        return result;
    }

    public static String[] beanNamesForTypeIncludingAncestors(
            ListableBeanFactory lbf, Class type, boolean includeNonSingletons, boolean allowEagerInit) {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                List<String> resultList = new ArrayList<String>();
                resultList.addAll(Arrays.asList(result));
                for (String beanName : parentResult) {
                    if (!resultList.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                        resultList.add(beanName);
                    }
                }
                result = StringUtils.toStringArray(resultList);
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<String, T>(4);
        result.putAll(lbf.getBeansOfType(type));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                for (Map.Entry<String, T> entry : parentResult.entrySet()) {
                    String beanName = entry.getKey();
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, entry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<String, T>(4);
        result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                for (Map.Entry<String, T> entry : parentResult.entrySet()) {
                    String beanName = entry.getKey();
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, entry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException {

        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
        if (beansOfType.size() == 1) {
            return beansOfType.values().iterator().next();
        }
        else {
            throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
        }
    }

    public static <T> T beanOfTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
        if (beansOfType.size() == 1) {
            return beansOfType.values().iterator().next();
        }
        else {
            throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
        }
    }

    public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type) throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type);
        if (beansOfType.size() == 1) {
            return beansOfType.values().iterator().next();
        }
        else {
            throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
        }
    }

    public static <T> T beanOfType(
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        if (beansOfType.size() == 1) {
            return beansOfType.values().iterator().next();
        }
        else {
            throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
        }
    }
}
