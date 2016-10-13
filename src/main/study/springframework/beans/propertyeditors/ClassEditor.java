package study.springframework.beans.propertyeditors;

import study.springframework.util.ClassUtils;
import study.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by pc on 2016/10/13.
 */
public class ClassEditor extends PropertyEditorSupport {

    private final ClassLoader classLoader;


    /**
     * Create a default ClassEditor, using the thread context ClassLoader.
     */
    public ClassEditor() {
        this(null);
    }

    /**
     * Create a default ClassEditor, using the given ClassLoader.
     * @param classLoader the ClassLoader to use
     * (or <code>null</code> for the thread context ClassLoader)
     */
    public ClassEditor(ClassLoader classLoader) {
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
        }
        else {
            setValue(null);
        }
    }

    @Override
    public String getAsText() {
        Class clazz = (Class) getValue();
        if (clazz != null) {
            return ClassUtils.getQualifiedName(clazz);
        }
        else {
            return "";
        }
    }
}
