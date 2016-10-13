package study.springframework.beans.propertyeditors;

import study.springframework.util.ClassUtils;
import study.springframework.util.ObjectUtils;
import study.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Created by pc on 2016/10/13.
 */
public class ClassArrayEditor extends PropertyEditorSupport {

    private final ClassLoader classLoader;


    /**
     * Create a default <code>ClassEditor</code>, using the thread
     * context <code>ClassLoader</code>.
     */
    public ClassArrayEditor() {
        this(null);
    }

    /**
     * Create a default <code>ClassArrayEditor</code>, using the given
     * <code>ClassLoader</code>.
     * @param classLoader the <code>ClassLoader</code> to use
     * (or pass <code>null</code> for the thread context <code>ClassLoader</code>)
     */
    public ClassArrayEditor(ClassLoader classLoader) {
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            String[] classNames = StringUtils.commaDelimitedListToStringArray(text);
            Class[] classes = new Class[classNames.length];
            for (int i = 0; i < classNames.length; i++) {
                String className = classNames[i].trim();
                classes[i] = ClassUtils.resolveClassName(className, this.classLoader);
            }
            setValue(classes);
        }
        else {
            setValue(null);
        }
    }

    @Override
    public String getAsText() {
        Class[] classes = (Class[]) getValue();
        if (ObjectUtils.isEmpty(classes)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.length; ++i) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(ClassUtils.getQualifiedName(classes[i]));
        }
        return sb.toString();
    }
}
