package study.springframework.core.io;

import com.sun.xml.internal.ws.api.ResourceLoader;
import study.springframework.util.Assert;
import study.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

/**
 * Created by pc on 2016/10/13.
 */
public class ResourceEditor extends PropertyEditorSupport {

    private final ResourceLoader resourceLoader;

    private final PropertyResolver propertyResolver;

    private final boolean ignoreUnresolvablePlaceholders;


    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using a {@link DefaultResourceLoader} and {@link StandardEnvironment}.
     */
    public ResourceEditor() {
        this(new DefaultResourceLoader(), new StandardEnvironment());
    }

    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using the given {@link ResourceLoader} and a {@link StandardEnvironment}.
     * @param resourceLoader the <code>ResourceLoader</code> to use
     * @deprecated as of Spring 3.1 in favor of
     * {@link #ResourceEditor(ResourceLoader, PropertyResolver)}
     */
    @Deprecated
    public ResourceEditor(ResourceLoader resourceLoader) {
        this(resourceLoader, new StandardEnvironment(), true);
    }

    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using the given {@link ResourceLoader} and {@link PropertyResolver}.
     * @param resourceLoader the <code>ResourceLoader</code> to use
     * @param propertyResolver the <code>PropertyResolver</code> to use
     */
    public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver) {
        this(resourceLoader, propertyResolver, true);
    }

    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using the given {@link ResourceLoader}.
     * @param resourceLoader the <code>ResourceLoader</code> to use
     * @param ignoreUnresolvablePlaceholders whether to ignore unresolvable placeholders
     * if no corresponding property could be found
     * @deprecated as of Spring 3.1 in favor of
     * {@link #ResourceEditor(ResourceLoader, PropertyResolver, boolean)}
     */
    @Deprecated
    public ResourceEditor(ResourceLoader resourceLoader, boolean ignoreUnresolvablePlaceholders) {
        this(resourceLoader, new StandardEnvironment(), ignoreUnresolvablePlaceholders);
    }

    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using the given {@link ResourceLoader}.
     * @param resourceLoader the <code>ResourceLoader</code> to use
     * @param propertyResolver the <code>PropertyResolver</code> to use
     * @param ignoreUnresolvablePlaceholders whether to ignore unresolvable placeholders
     * if no corresponding property could be found in the given <code>propertyResolver</code>
     */
    public ResourceEditor(ResourceLoader resourceLoader, PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders) {
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        Assert.notNull(propertyResolver, "PropertyResolver must not be null");
        this.resourceLoader = resourceLoader;
        this.propertyResolver = propertyResolver;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }


    @Override
    public void setAsText(String text) {
        if (StringUtils.hasText(text)) {
            String locationToUse = resolvePath(text).trim();
            setValue(this.resourceLoader.getResource(locationToUse));
        }
        else {
            setValue(null);
        }
    }

    /**
     * Resolve the given path, replacing placeholders with corresponding
     * property values from the <code>environment</code> if necessary.
     * @param path the original file path
     * @return the resolved file path
     * @see PropertyResolver#resolvePlaceholders
     * @see PropertyResolver#resolveRequiredPlaceholders
     */
    protected String resolvePath(String path) {
        return this.ignoreUnresolvablePlaceholders ?
                this.propertyResolver.resolvePlaceholders(path) :
                this.propertyResolver.resolveRequiredPlaceholders(path);
    }


    @Override
    public String getAsText() {
        Resource value = (Resource) getValue();
        try {
            // Try to determine URL for resource.
            return (value != null ? value.getURL().toExternalForm() : "");
        }
        catch (IOException ex) {
            // Couldn't determine resource URL - return null to indicate
            // that there is no appropriate text representation.
            return null;
        }
    }
}
