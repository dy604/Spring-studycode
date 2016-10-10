package study.springframework.beans.factory;

/**
 * Created by dy on 2016/10/10.
 */
public interface DisposableBean {

    void destroy() throws Exception;
}
