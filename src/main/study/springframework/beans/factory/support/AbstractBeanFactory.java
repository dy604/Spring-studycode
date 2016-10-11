package study.springframework.beans.factory.support;

import study.springframework.beans.factory.BeanFactory;
import study.springframework.beans.factory.ObjectFactory;
import study.springframework.beans.factory.config.BeanExpressionResolver;
import study.springframework.beans.factory.config.ConfigurableBeanFactory;
import study.springframework.beans.factory.config.Scope;
import study.springframework.core.convert.ConversionService;
import study.springframework.util.ClassUtils;
import study.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by dy on 2016/10/11.
 */
public class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private BeanFactory parentBeanFactory;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private ClassLoader tempClassLoader;

    private boolean cacheBeanMetadata = true;

    private BeanExpressionResolver beanExpressionResolver;

    private ConversionService conversionService;

    private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet<PropertyEditorRegistrar>(4);

    private TypeConverter typeConverter;

    private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<Class<?>, Class<? extends PropertyEditor>>(4);

    private final List<StringValueResolver> embeddedValueResolvers = new LinkedList<StringValueResolver>();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    private boolean hasInstantiationAwareBeanPostProcessors;

    private final Map<String, Scope> scopes = new HashMap<String, Scope>(8);

    private SecurityContextProvider securityContextProvider;

    private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<String, RootBeanDefinition>(64);

    private final Map<String, Boolean> alreadyCreated = new ConcurrentHashMap<String, Boolean>(64);

    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal<Object>("Prototype beans currently in creation");


}
