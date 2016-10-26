package study.springframework.beans;

import study.springframework.core.ErrorCoded;

import java.beans.PropertyChangeEvent;

/**
 * Created by pc on 2016/10/13.
 */
public abstract class PropertyAccessException extends BeansException implements ErrorCoded {

    private transient PropertyChangeEvent propertyChangeEvent;

    public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause) {
        super(msg, cause);
        this.propertyChangeEvent = propertyChangeEvent;
    }

    public PropertyAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PropertyChangeEvent getPropertyChangeEvent() {
        return this.propertyChangeEvent;
    }

    public String getPropertyName() {
        return (this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null);
    }

    public Object getValue() {
        return (this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null);
    }
}
