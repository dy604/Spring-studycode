package study.springframework.cglib.core;

import org.objectweb.asm.ClassVisitor;

/**
 * Created by dy on 2016/10/27.
 */
public interface ClassGenerator {

    void generateClass(ClassVisitor v) throws Exception;
}
