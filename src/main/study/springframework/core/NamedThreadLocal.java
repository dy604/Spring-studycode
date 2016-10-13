package study.springframework.core;

import study.springframework.util.Assert;

/**
 * Created by pc on 2016/10/13.
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {

    private final String name;

    public NamedThreadLocal(String name) {
        Assert.hasText(name, "Name must not be empty");
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
