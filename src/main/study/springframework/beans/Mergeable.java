package study.springframework.beans;

/**
 * Created by dy on 2016/10/11.
 */
public interface Mergeable {

    boolean isMergeEnabled();

    Object merge(Object parent);
}
