package study.springframework.core;

import java.io.IOException;

/**
 * Created by pc on 2016/10/8.
 */
public class NestedIOException extends IOException {

    static {
        NestedExceptionUtils.class.getName();
    }

    public NestedIOException(String msg) {
        super(msg);
    }

    public NestedIOException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }

    @Override
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }
}
