package study.springframework.core.io;

/**
 * Created by dy on 2016/10/10.
 */
public interface ContextResource extends Resource {

    String getPathWithinContext();
}
