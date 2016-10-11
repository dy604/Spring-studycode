package study.springframework.beans.factory.config;

import study.springframework.beans.BeanMetadataElement;
import study.springframework.beans.Mergeable;
import study.springframework.util.Assert;
import study.springframework.util.ClassUtils;
import study.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created by dy on 2016/10/11.
 */
public class ConstructorArgumentValues {

    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<Integer, ValueHolder>(0);

    private final List<ValueHolder> genericArgumentValues = new LinkedList<ValueHolder>();

    public ConstructorArgumentValues() {
    }

    public ConstructorArgumentValues(ConstructorArgumentValues original) {
        addArgumentValues(original);
    }

    public void addArgumentValues(ConstructorArgumentValues other) {
        if (other != null) {
            for (Map.Entry<Integer, ValueHolder> entry : other.indexedArgumentValues.entrySet()) {
                addOrMergeIndexedArgumentValue(entry.getKey(), entry.getValue().copy());
            }
            for (ValueHolder valueHolder : other.genericArgumentValues) {
                if (!this.genericArgumentValues.contains(valueHolder)) {
                    addOrMergeGenericArgumentValue(valueHolder.copy());
                }
            }
        }
    }

    public void addIndexedArgumentValue(int index, Object value) {
        addIndexedArgumentValue(index, new ValueHolder(value));
    }

    public void addIndexedArgumentValue(int index, Object value, String type) {
        addIndexedArgumentValue(index, new ValueHolder(value, type));
    }

    public void addIndexedArgumentValue(int index, ValueHolder newValue) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        Assert.notNull(newValue, "ValueHolder must not be null");
        addOrMergeIndexedArgumentValue(index, newValue);
    }

    private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
        ValueHolder currentValue = this.indexedArgumentValues.get(key);
        if (currentValue != null && newValue.getValue() instanceof Mergeable) {
            Mergeable mergeable = (Mergeable) newValue.getValue();
            if (mergeable.isMergeEnabled()) {
                newValue.setValue(mergeable.merge(currentValue.getValue()));
            }
        }
        this.indexedArgumentValues.put(key, newValue);
    }

    public boolean hasIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.containsKey(index);
    }

    public ValueHolder getIndexedArgumentValue(int index, Class requiredType) {
        return getIndexedArgumentValue(index, requiredType, null);
    }

    public ValueHolder getIndexedArgumentValue(int index, Class requiredType, String requiredName) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = this.indexedArgumentValues.get(index);
        if (valueHolder != null &&
                (valueHolder.getType() == null ||
                        (requiredType != null && ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) &&
                (valueHolder.getName() == null ||
                        (requiredName != null && requiredName.equals(valueHolder.getName())))) {
            return valueHolder;
        }
        return null;
    }

    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return Collections.unmodifiableMap(this.indexedArgumentValues);
    }

    public void addGenericArgumentValue(Object value) {
        this.genericArgumentValues.add(new ValueHolder(value));
    }

    public void addGenericArgumentValue(Object value, String type) {
        this.genericArgumentValues.add(new ValueHolder(value, type));
    }

    public void addGenericArgumentValue(ValueHolder newValue) {
        Assert.notNull(newValue, "ValueHolder must not be null");
        if (!this.genericArgumentValues.contains(newValue)) {
            addOrMergeGenericArgumentValue(newValue);
        }
    }

    private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
        if (newValue.getName() != null) {
            for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext();) {
                ValueHolder currentValue = it.next();
                if (newValue.getName().equals(currentValue.getName())) {
                    if (newValue.getValue() instanceof Mergeable) {
                        Mergeable mergeable = (Mergeable) newValue.getValue();
                        if (mergeable.isMergeEnabled()) {
                            newValue.setValue(mergeable.merge(currentValue.getValue()));
                        }
                    }
                    it.remove();
                }
            }
        }
        this.genericArgumentValues.add(newValue);
    }

    public ValueHolder getGenericArgumentValue(Class requiredType) {
        return getGenericArgumentValue(requiredType, null, null);
    }

    public ValueHolder getGenericArgumentValue(Class requiredType, String requiredName) {
        return getGenericArgumentValue(requiredType, requiredName, null);
    }

    public ValueHolder getGenericArgumentValue(Class requiredType, String requiredName, Set<ValueHolder> usedValueHolders) {
        for (ValueHolder valueHolder : this.genericArgumentValues) {
            if (usedValueHolders != null && usedValueHolders.contains(valueHolder)) {
                continue;
            }
            if (valueHolder.getName() != null &&
                    (requiredName == null || !valueHolder.getName().equals(requiredName))) {
                continue;
            }
            if (valueHolder.getType() != null &&
                    (requiredType == null || !ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
                continue;
            }
            if (requiredType != null && valueHolder.getType() == null && valueHolder.getName() == null &&
                    !ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
                continue;
            }
            return valueHolder;
        }
        return null;
    }

    public List<ValueHolder> getGenericArgumentValues() {
        return Collections.unmodifiableList(this.genericArgumentValues);
    }

    public ValueHolder getArgumentValue(int index, Class requiredType) {
        return getArgumentValue(index, requiredType, null, null);
    }

    public ValueHolder getArgumentValue(int index, Class requiredType, String requiredName) {
        return getArgumentValue(index, requiredType, requiredName, null);
    }

    public ValueHolder getArgumentValue(int index, Class requiredType, String requiredName, Set<ValueHolder> usedValueHolders) {
        Assert.isTrue(index >= 0, "Index must not be negative");
        ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
        if (valueHolder == null) {
            valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
        }
        return valueHolder;
    }

    public int getArgumentCount() {
        return (this.indexedArgumentValues.size() + this.genericArgumentValues.size());
    }

    public boolean isEmpty() {
        return (this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty());
    }

    public void clear() {
        this.indexedArgumentValues.clear();
        this.genericArgumentValues.clear();
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ConstructorArgumentValues)) {
            return false;
        }
        ConstructorArgumentValues that = (ConstructorArgumentValues) other;
        if (this.genericArgumentValues.size() != that.genericArgumentValues.size() ||
                this.indexedArgumentValues.size() != that.indexedArgumentValues.size()) {
            return false;
        }
        Iterator<ValueHolder> it1 = this.genericArgumentValues.iterator();
        Iterator<ValueHolder> it2 = that.genericArgumentValues.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            ValueHolder vh1 = it1.next();
            ValueHolder vh2 = it2.next();
            if (!vh1.contentEquals(vh2)) {
                return false;
            }
        }
        for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
            ValueHolder vh1 = entry.getValue();
            ValueHolder vh2 = that.indexedArgumentValues.get(entry.getKey());
            if (!vh1.contentEquals(vh2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 7;
        for (ValueHolder valueHolder : this.genericArgumentValues) {
            hashCode = 31 * hashCode + valueHolder.contentHashCode();
        }
        hashCode = 29 * hashCode;
        for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
            hashCode = 31 * hashCode + (entry.getValue().contentHashCode() ^ entry.getKey().hashCode());
        }
        return hashCode;
    }

    public static class ValueHolder implements BeanMetadataElement {

        private Object value;

        private String type;

        private String name;

        private Object source;

        private boolean converted = false;

        private Object convertedValue;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public ValueHolder(Object value, String type) {
            this.value = value;
            this.type = type;
        }

        public ValueHolder(Object value, String type, String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getSource() {
            return this.source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public synchronized boolean isConverted() {
            return this.converted;
        }

        public synchronized Object getConvertedValue() {
            return this.convertedValue;
        }

        public synchronized void setConvertedValue(Object value) {
            this.converted = true;
            this.convertedValue = value;
        }

        private boolean contentEquals(ValueHolder other) {
            return (this == other ||
                    (ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, other.type)));
        }

        private int contentHashCode() {
            return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
        }

        public ValueHolder copy() {
            ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
            copy.setSource(this.source);
            return copy;
        }
    }
}
