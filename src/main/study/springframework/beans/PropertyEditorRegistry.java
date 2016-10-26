package study.springframework.beans;

import java.beans.PropertyEditor;

/**
 * Created by pc on 2016/10/13.
 */
public interface PropertyEditorRegistry {

    void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor);

    void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor);

    PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath);
}
