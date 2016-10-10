package study.springframework.beans;

/**
 * Created by dy on 2016/10/10.
 */
public class FatalBeanException extends BeansException {

    public FatalBeanException(String msg) {
        super(msg);
    }

    public FatalBeanException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
