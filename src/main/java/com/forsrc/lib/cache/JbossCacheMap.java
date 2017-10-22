package com.forsrc.lib.cache;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.jboss.cache.Cache;
import org.jboss.cache.CacheFactory;
import org.jboss.cache.DefaultCacheFactory;
import org.jboss.cache.Fqn;

public class JbossCacheMap<K extends Serializable, V extends Serializable> implements Map<K, V>, Closeable {

    private Cache<K, V> cache;
    private Fqn<String> fqn;

    public JbossCacheMap(String name) {
        CacheFactory<K, V> factory = new DefaultCacheFactory<>();
        this.cache = factory.createCache("config/jboss-cache.xml", false);
        this.cache.create();
        this.cache.start();
        this.fqn = Fqn.fromString(name);
    }

    public Cache<K, V> getCache() {
        return cache;
    }

    public void setCache(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public int size() {
        return this.cache.getData(this.fqn).size();
    }

    @Override
    public boolean isEmpty() {
        return this.cache.getData(this.fqn).isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.cache.getData(this.fqn).containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.cache.getData(this.fqn).containsValue(value);
    }

    public V get(K key) {
        return this.cache.get(this.fqn, key);
    }

    @Override
    public V get(Object key) {
        return this.cache.get(this.fqn, (K)key);
    }

    @Override
    public V put(K key, V value) {
        return this.cache.put(this.fqn, key, value);
    }

    public V remove(K key) {
        return this.cache.remove(this.fqn, key);
    }

    @Override
    public V remove(Object key) {
        return this.cache.remove(this.fqn, (K)key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.cache.put(this.fqn, m);;
    }

    @Override
    public void clear() {
        this.cache.clearData(this.fqn);
    }

    @Override
    public Set<K> keySet() {
        return this.cache.getKeys(this.fqn);
    }

    @Override
    public Collection<V> values() {
        Map<K, V> map = this.cache.getData(this.fqn);
        if (map == null) {
            return Collections.emptyList();
        }
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Map<K, V> map = this.cache.getData(this.fqn);
        if (map == null) {
            return ((Map<K, V>)Collections.emptyMap()).entrySet();
        }
        return map.entrySet();
    }

    @Override
    public void close() throws IOException {
        this.cache.stop();
    }

}
