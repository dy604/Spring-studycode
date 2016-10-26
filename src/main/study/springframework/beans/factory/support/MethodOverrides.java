package study.springframework.beans.factory.support;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pc on 2016/10/13.
 */
public class MethodOverrides {

    private final Set<MethodOverride> overrides = new HashSet<MethodOverride>(0);

    public MethodOverrides() {

    }

    public MethodOverrides(MethodOverrides other) {
        addOverrides(other);
    }

    public void addOverrides(MethodOverrides other) {
        if (other != null) {
            this.overrides.addAll(other.getOverrides());
        }
    }

    public void addOverride(MethodOverride override) {
        this.overrides.add(override);
    }

    public Set<MethodOverride> getOverrides() {
        return this.overrides;
    }

    public boolean isEmpty() {
        return this.overrides.isEmpty();
    }

    public MethodOverride getOverride(Method method) {
        for (MethodOverride override : this.overrides) {
            if (override.matches(method)) {
                return override;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return this.overrides.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodOverrides)) {
            return false;
        }
        MethodOverrides that = (MethodOverrides) other;
        return this.overrides.equals(that.overrides);
    }
}
