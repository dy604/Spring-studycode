package study.springframework.core;

import study.springframework.util.Assert;
import study.springframework.util.StringUtils;
import study.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dy on 2016/10/10.
 */
public class SimpleAliasRegistry implements AliasRegistry {

    private final Map<String, String> aliasMap = new ConcurrentHashMap<String, String>(16);

    public void registryAlias(String name, String alias) {
        Assert.hasText(name, "'name' must not be empty");
        Assert.hasText(alias, "'alias' must not be empty");
        if (alias.equals(name)) {
            this.aliasMap.remove(alias);
        } else {
            if (!allowAliasOverriding()) {
                String registeredName = this.aliasMap.get(alias);
                if (registeredName != null && !registeredName.equals(name)) {
                    throw new IllegalStateException("Cannot registry alias '" + alias + "' for name '" +
                        name + "': It is already registered for name '" + registeredName + "'.");
                }
            }
            checkForAliasCurcle(name, alias);
            this.aliasMap.put(alias, name);
        }
    }

    protected boolean allowAliasOverriding() {
        return true;
    }

    public void removeAlias(String alias) {
        String name = this.aliasMap.remove(alias);
        if (name == null) {
            throw new IllegalArgumentException("No alias '" + alias + "' registered");
        }
    }

    public boolean isAlias(String name) {
        return this.aliasMap.containsKey(name);
    }

    public String[] getAliases(String name) {
        List<String> result = new ArrayList<String>();
        synchronized (this.aliasMap) {
            retrieveAliases(name, result);
        }
        return StringUtils.toStringArray(result);
    }

    protected void retrieveAliases(String name, List<String> result) {
        for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
            String registeredName = entry.getValue();
            if (registeredName.equals(name)) {
                String alias = entry.getKey();
                result.add(alias);
                retrieveAliases(alias, result);
            }
        }
    }

    public void resolveAliases(StringValueResolver valueResolver) {
        Assert.notNull(valueResolver, "StringValueResolver must not be null");
        synchronized (this.aliasMap) {
            Map<String, String> aliasCopy = new HashMap<String, String>(this.aliasMap);
            for (String alias : aliasCopy.keySet()) {
                String registerName = aliasCopy.get(alias);
                String resolvedAlias = valueResolver.resolveStringValue(alias);
                String resolvedName = valueResolver.resolveStringValue(registerName);
                if (resolvedAlias.equals(registerName)) {
                    this.aliasMap.remove(alias);
                } else if (!resolvedAlias.equals(alias)) {
                    String existingName = this.aliasMap.get(resolvedAlias);
                    if (existingName != null && !existingName.equals(resolvedName)) {
                        throw new IllegalStateException(
                                "Cannot registry resolved alias '" + resolvedAlias + "' (original: '" + alias +
                                "') for name '" + resolvedName + "': It is already registered for name '" +
                                registerName + "'.");
                    }
                    checkForAliasCircle(resolvedName, resolvedAlias);
                    this.aliasMap.remove(alias);
                    this.aliasMap.put(resolvedAlias, resolvedName);
                } else if (!registerName.equals(resolvedName)) {
                    this.aliasMap.put(alias, resolvedName);
                }
            }
        }
    }

    public String canonicalName(String name) {
        String canonicalName = name;
        String resolvedName;
        do {
            resolvedName = this.aliasMap.get(canonicalName);
            if (resolvedName != null) {
                canonicalName = resolvedName;
            }
        } while (resolvedName != null);
        return canonicalName;
    }

    protected void checkForAliasCurcle(String name, String alias) {
        if (alias.equals(canonicalName(name))) {
            throw new IllegalStateException("Cannot registry alias '" + alias +
            "' for name '" + name + "': Circular reference - '" +
            name + "' is a direct or indirect alias for '" + alias + "' already");
        }
    }
}
