package study.springframework.cglib.core;

/**
 * Created by dy on 2016/10/27.
 */
public class CodeGenerationException extends RuntimeException {

    private Throwable cause;

    public CodeGenerationException(Throwable cause) {
        super(cause.getClass().getName() + "-->" + cause.getMessage());
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
