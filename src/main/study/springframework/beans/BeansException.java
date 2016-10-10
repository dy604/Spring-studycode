package study.springframework.beans;

/**
 * Created by dy on 2016/10/10.
 */

import study.springframework.core.NestedRuntimeException;
import study.springframework.util.ObjectUtils;

x
public abstract class BeansException extends NestedRuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeansException)) {
            return false;
        }
        BeansException otherBe = (BeansException) other;
        return (getMessage().equals(otherBe.getMessage()) && ObjectUtils.nullSafeEquals(getCause(), otherBe.getCause()));
    }

    @Override
    public int hashCode() {
        return getMessage().hashCode();
    }
}
