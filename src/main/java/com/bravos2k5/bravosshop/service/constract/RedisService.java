package com.bravos2k5.bravosshop.service.constract;

import com.bravos2k5.bravosshop.cache.RedisCacheEntry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    void save(String key, Object value);

    void save(String key, Object value, long timeout, TimeUnit timeUnit);

    boolean saveIfAbsent(String key, Object value);

    boolean saveIfAbsent(String key, Object value, long timeout, TimeUnit timeUnit);

    boolean hasKey(String key);

    <T> T get(String key);

    <T> List<T> multiGet(Collection<String> key);

    <T> T getAndSet(String key, Object value);

    <T> T getAndDelete(String key);

    <T extends Number> T increment(String key, long delta, Class<T> type);

    <T extends Number> T decrement(String key, long delta, Class<T> type);

    <T extends Number> T increment(String key, double delta, Class<T> type);

    void delete(String key);

    void clearAll();

    <T> T getWithLock(RedisCacheEntry<T> cacheEntry);

}
