package study.springframework.beans.factory.support;

import org.apache.commons.logging.LogFactory;
import study.springframework.beans.factory.BeanFactory;
import study.springframework.cglib.proxy.Enhancer;
import sun.rmi.runtime.Log;

import javax.security.auth.callback.Callback;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by dy on 2016/10/26.
 */
public class CglibSubclassingInstantiationStrategy extends SimpleInstantiationStrategy {

    private static final int PASSTHROUGH = 0;
    private static final int LOOKUP_OVERRIDE = 1;
    private static final int METHOD_REPLACER = 2;

    @Override
    protected Object instantiateWithMethodInjection(
            RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) {

        // Must generate CGLIB subclass.
        return new CglibSubclassCreator(beanDefinition, owner).instantiate(null, null);
    }

    @Override
    protected Object instantiateWithMethodInjection(
            RootBeanDefinition beanDefinition, String beanName, BeanFactory owner,
            Constructor ctor, Object[] args) {

        return new CglibSubclassCreator(beanDefinition, owner).instantiate(ctor, args);
    }

    private static class CglibSubclassCreator {
        private static final Log logger = LogFactory.getLog(CglibSubclassCreator.class);
        private final RootBeanDefinition beanDefinition;
        private final BeanFactory owner;

        public CglibSubclassCreator(RootBeanDefinition beanDefinition, BeanFactory owner) {
            this.beanDefinition = beanDefinition;
            this.owner = owner;
        }

        public Object instantiate(Constructor ctor, Object[] args) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(this.beanDefinition.getBeanClass());
            enhancer.setCallbackFilter(new CallbackFilterImpl());
            enhancer.setCallbacks(new Callback[] {
                    NoOp.INSTANCE,
                    new LookupOverrideMethodInterceptor(),
                    new ReplaceOverrideMethodInterceptor()
            });

            return (ctor == null) ?
                    enhancer.create() :
                    enhancer.create(ctor.getParameterTypes(), args);
        }
    }

    private class CglibIdentitySupport {
        protected RootBeanDefinition getBeanDefinition() {
            return beanDefinition;
        }

        @Override
        public boolean equals(Object other) {
            return (other.getClass().equals(getClass()) &&
                    ((CglibIdentitySupport) other).getBeanDefinition().equals(beanDefinition));
        }

        @Override
        public int hashCode() {
            return beanDefinition.hashCode();
        }
    }

    private class LookupOverrideMethodInterceptor extends CglibIdentitySupport implements MethodInterceptor {

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
            // Cast is safe, as CallbackFilter filters are used selectively.
            LookupOverride lo = (LookupOverride) beanDefinition.getMethodOverrides().getOverride(method);
            return owner.getBean(lo.getBeanName());
        }
    }

    private class ReplaceOverrideMethodInterceptor extends CglibIdentitySupport implements MethodInterceptor {

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
            ReplaceOverride ro = (ReplaceOverride) beanDefinition.getMethodOverrides().getOverride(method);
            // TODO could cache if a singleton for minor performance optimization
            MethodReplacer mr = (MethodReplacer) owner.getBean(ro.getMethodReplacerBeanName());
            return mr.reimplement(obj, method, args);
        }
    }

    private class CallbackFilterImpl extends CglibIdentitySupport implements CallbackFilter {

        public int accept(Method method) {
            MethodOverride methodOverride = beanDefinition.getMethodOverrides().getOverride(method);
            if (logger.isTraceEnabled()) {
                logger.trace("Override for '" + method.getName() + "' is [" + methodOverride + "]");
            }
            if (methodOverride == null) {
                return PASSTHROUGH;
            }
            else if (methodOverride instanceof
                    LookupOverride) {
                return LOOKUP_OVERRIDE;
            }
            else if (methodOverride instanceof ReplaceOverride) {
                return METHOD_REPLACER;
            }
            throw new UnsupportedOperationException(
                    "Unexpected MethodOverride subclass: " + methodOverride.getClass().getName());
        }
    }
}
