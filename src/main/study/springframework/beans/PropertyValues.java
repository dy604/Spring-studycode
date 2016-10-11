package study.springframework.beans;

/**
 * Created by dy on 2016/10/11.
 */
public interface PropertyValues {

    PropertyValue[] getPropertyValues();

    PropertyValue getPropertyValue(String propertyName);

    PropertyValue changesSince(PropertyValues old);

    boolean contains(String propertyName);

    boolean isEmpty();
}
