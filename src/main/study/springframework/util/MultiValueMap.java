package study.springframework.util;

import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2016/10/8.
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    V getFirst(K key);

    void add(K key, V value);

    void set(K key, V value);

    void setAll(Map<K, V> values);

    Map<K, V> toSingleValueMap();
}
