package study.springframework.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import study.springframework.beans.BeanCreationException;
import study.springframework.beans.factory.BeanCreationNotAllowedException;
import study.springframework.beans.factory.BeanCurrentlyInCreationException;
import study.springframework.beans.factory.DisposableBean;
import study.springframework.beans.factory.ObjectFactory;
import study.springframework.beans.factory.config.SingletonBeanRegistry;
import study.springframework.core.SimpleAliasRegistry;
import study.springframework.util.Assert;
import study.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dy on 2016/10/10.
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();

    protected final Log logger = LogFactory.getLog(getClass());

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);

    private final Map<String, ObjectFactory> singletonFactories = new HashMap<String, ObjectFactory>(16);

    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    private final Set<String> registeredSingletons = new LinkedHashSet<String>(64);

    private final Map<String, Boolean> singletonsCurrentlyInCreation = new ConcurrentHashMap<String, Boolean>(16);

    private final Map<String, Boolean> inCreationCheckExclusions = new ConcurrentHashMap<String, Boolean>(16);

    private Set<Exception> suppressedExceptions;

    private boolean singletonsCurrentlyInDestruction = false;

    private final Map<String, Object> disposableBeans = new LinkedHashMap<String, Object>();

    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<String, Set<String>>(16);

    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<String, Set<String>>(64);

    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<String, Set<String>>(64);

    public void registrySingleton(String beanName, Object singletonObject) throws IllegalStateException {
        Assert.notNull(beanName, "'beanName' must not be null");
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            addSingleton(beanName, singletonObject);
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    protected void addSingletonFactory(String beanName, ObjectFactory singletonFactory) {
        Assert.notNull(singletonFactory, "Singleton factory must not be null");
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }

    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObjects == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                singletonObject = this.earlySingletonObjects.get(beanName);
                if (singletonObject == null && allowEarlyReference) {
                    ObjectFactory singletonFactory = this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        singletonObject = singletonFactory.getClass();
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return (singletonObject != NULL_OBJECT ? singletonObject : null);
    }

    public Object getSingleton(String beanName, ObjectFactory singletonFactory) {
        Assert.notNull(beanName, "'beanName' must not be null");
        synchronized (this.singletonObjects) {
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                if (this.singletonsCurrentlyInDestruction) {
                    throw new BeanCreationNotAllowedException(beanName,
                            "Singleton bean creation not allowed while the singletons of this factory are in destruction " +
                            "(Do not request a bean from a BeanFactory in a destroy method implementation!");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
                }
                beforeSingletonCreation(beanName);
                boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
                if (recordSuppressedExceptions) {
                    this.suppressedExceptions = new LinkedHashSet<Exception>();
                }
                try {
                    singletonObject = singletonFactory.getObject();
                } catch (BeanCreationException ex) {
                    if (recordSuppressedExceptions) {
                        for (Exception suppressedException : this.suppressedExceptions) {
                            ex.addRelatedCause(suppressedException);
                        }
                    }
                    throw ex;
                } finally {
                    if (recordSuppressedExceptions) {
                        this.suppressedExceptions = null;
                    }
                    afterSingletonCreation(beanName);
                }
                addSingleton(beanName, singletonObject);
            }
            return (singletonObject != NULL_OBJECT ? singletonObject : null);
        }
    }

    protected void onSuppressedException(Exception ex) {
        synchronized (this.singletonObjects) {
            if (this.suppressedExceptions != null) {
                this.suppressedExceptions.add(ex);
            }
        }
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
    }

    public boolean containsSingleton(String beanName) {
        return (this.singletonObjects.containsKey(beanName));
    }

    public String[] getSingletonNames() {
        synchronized (this.singletonObjects) {
            return StringUtils.toStringArray(this.registeredSingletons);
        }
    }

    public int getSingletonCount() {
        synchronized (this.singletonObjects) {
            return this.registeredSingletons.size();
        }
    }

    public void setSingletonCurrentlyInCreation(String beanName, boolean inCreation) {
        Assert.notNull(beanName, "Bean name must not be null");
        if (!inCreation) {
            this.inCreationCheckExclusions.remove(beanName);
        } else {
            this.inCreationCheckExclusions.remove(beanName);
        }
    }

    public boolean isCurrentlyInCreation(String beanName) {
        Assert.notNull(beanName, "Bean name must not be null");
        return (!this.inCreationCheckExclusions.containsKey(beanName) && isActuallyInCreation(beanName));
    }

    protected boolean isActuallyInCreation(String beanName) {
        return isSingletonCurrentlyInCreation(beanName);
    }

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.containsKey(beanName);
    }

    protected void beforeSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.containsKey(beanName) &&
                this.singletonsCurrentlyInCreation.put(beanName, Boolean.TRUE) != null) {
            throw new BeanCurrentlyInCreationException(beanName);
        }
    }

    protected void afterSingletonCreation(String beanName) {
        if (!this.inCreationCheckExclusions.containsKey(beanName) &&
                !this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        synchronized (this.disposableBeans) {
            this.disposableBeans.put(beanName, bean);
        }
    }

    public void registryContainedBean(String containedBeanName, String containingBeanName) {
        synchronized (this.containedBeanMap) {
            Set<String> containedBeans = this.containedBeanMap.get(containedBeanName);
            if (containedBeans == null) {
                containedBeans = new LinkedHashSet<String>(8);
                this.containedBeanMap.put(containedBeanName, containedBeans);
            }
            containedBeans.add(containedBeanName);
        }
        registerDependentBean(containedBeanName, containingBeanName);
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {
        String canonicalName = canonicalName(beanName);
        synchronized (this.dependentBeanMap) {
            Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<String>(8);
                this.dependentBeanMap.put(canonicalName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<String>(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(canonicalName);
        }
    }

    protected boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }

    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return StringUtils.toStringArray(dependentBeans);
    }

    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return dependenciesForBean.toArray(new String[dependenciesForBean.size()]);
    }

    public void destroySingletons() {
        if (logger.isInfoEnabled()) {
            logger.info("Destroying singletons in " + this);
        }
        synchronized (this.singletonObjects) {
            this.singletonsCurrentlyInDestruction = true;
        }

        String[] disposableBeanNames;
        synchronized (this.disposableBeans) {
            disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
        }
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            destroySingleton(disposableBeanNames[i]);
        }

        this.containedBeanMap.clear();
        this.dependentBeanMap.clear();
        this.dependenciesForBeanMap.clear();

        synchronized (this.singletonObjects) {
            this.singletonObjects.clear();
            this.singletonFactories.clear();
            this.earlySingletonObjects.clear();
            this.registeredSingletons.clear();
            this.singletonsCurrentlyInDestruction = false;
        }
    }

    public void destroySingleton(String beanName) {
        // Remove a registered singleton of the given name, if any.
        removeSingleton(beanName);

        // Destroy the corresponding DisposableBean instance.
        DisposableBean disposableBean;
        synchronized (this.disposableBeans) {
            disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
        }
        destroyBean(beanName, disposableBean);
    }

    protected void destroyBean(String beanName, DisposableBean bean) {
        // Trigger destruction of dependent beans first...
        Set<String> dependencies = this.dependentBeanMap.remove(beanName);
        if (dependencies != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
            }
            for (String dependentBeanName : dependencies) {
                destroySingleton(dependentBeanName);
            }
        }

        // Actually destroy the bean now...
        if (bean != null) {
            try {
                bean.destroy();
            }
            catch (Throwable ex) {
                logger.error("Destroy method on bean with name '" + beanName + "' threw an exception", ex);
            }
        }

        // Trigger destruction of contained beans...
        Set<String> containedBeans = this.containedBeanMap.remove(beanName);
        if (containedBeans != null) {
            for (String containedBeanName : containedBeans) {
                destroySingleton(containedBeanName);
            }
        }

        // Remove destroyed bean from other beans' dependencies.
        synchronized (this.dependentBeanMap) {
            for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Set<String>> entry = it.next();
                Set<String> dependenciesToClean = entry.getValue();
                dependenciesToClean.remove(beanName);
                if (dependenciesToClean.isEmpty()) {
                    it.remove();
                }
            }
        }

        // Remove destroyed bean's prepared dependency information.
        this.dependenciesForBeanMap.remove(beanName);
    }

    protected final Object getSingletonMutex() {
        return this.singletonObjects;
    }
}
