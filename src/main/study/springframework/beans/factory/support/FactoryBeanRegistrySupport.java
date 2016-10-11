package study.springframework.beans.factory.support;

import study.springframework.beans.BeanCreationException;
import study.springframework.beans.BeansException;
import study.springframework.beans.factory.BeanCurrentlyInCreationException;
import study.springframework.beans.factory.FactoryBean;
import study.springframework.beans.factory.FactoryBeanNotInitializedException;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dy on 2016/10/11.
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);

    protected Class getTypeForFactoryBean(final FactoryBean factoryBean) {
        try {
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged(new PrivilegedAction<Class>() {

                    public Class run() {
                        return factoryBean.getObjectType();
                    }
                }, getAccessControlContext());
            } else {
                return factoryBean.getObjectType();
            }
        } catch (Throwable ex) {
            logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying " +
                    "that it should return null if the type of its object cannot be determined yet", ex);
            return null;
        }
    }

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object != NULL_OBJECT ? object : null);
    }

    protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName, boolean shouldPostProcess) {
        if (factory.isSingleton() && containsSingleton(beanName)) {
            synchronized (getSingletonMutex()) {
                Object object = this.factoryBeanObjectCache.get(beanName);
                if (object == null) {
                    object = doGetObjectFromFactoryBean(factory, beanName, shouldPostProcess);
                    this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
                }
                return (object != NULL_OBJECT ? object : null);
            }
        } else {
            return doGetObjectFromFactoryBean(factory, beanName, shouldPostProcess);
        }
    }

    private Object doGetObjectFromFactoryBean(
        final FactoryBean factory, final String beanName, final boolean shouldPostProcess)
        throws BeanCreationException {

        Object object;
        try {
            if (System.getSecurityManager() != null) {
                AccessControlContext acc = getAccessControlContext();
                try {
                    object = AccessController.doPrivileged(new PrivilegedAction<Object>() {

                        public Object run() throws Exception{
                            return factory.getObject();
                        }
                    }, acc);
                } catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            } else {
                object = factory.getObject();
            }
        } catch (FactoryBeanNotInitializedException ex) {
            throw new BeanCurrentlyInCreationException(beanName, ex.toString());
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
        }

        if (object == null && isSingletonCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(
                    beanName, "FactoryBean which is currently in creation returned null from getObject");
        }

        if (object != null && shouldPostProcess) {
            try {
                object  = postProcessObjectFromFactoryBean(object, beanName);
            } catch (Throwable ex) {
                throw new BeanCreationException(beanName, "Post-processing of the FactoryBean's object failed", ex);
            }
        }

        return object;
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }

    protected FactoryBean getFactoryBean(String beanName, Object beanInstance) throws BeansException {
        if (!(beanInstance instanceof FactoryBean)) {
            throw new BeanCreationException(beanName,
                    "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (FactoryBean) beanInstance;
    }

    @Override
    protected void removeSingleton(String beanName) {
        super.removeSingleton(beanName);
        this.factoryBeanObjectCache.remove(beanName);
    }

    protected AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }
}
