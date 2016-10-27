package study.springframework.cglib.core;

/**
 * Created by dy on 2016/10/27.
 */
public interface GeneratorStrategy {

    byte[] generate(ClassGenerator cg) throws Exception;

    boolean equals(Object o);
}
