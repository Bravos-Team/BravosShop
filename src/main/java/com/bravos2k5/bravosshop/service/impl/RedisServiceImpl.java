package com.bravos2k5.bravosshop.service.impl;

import com.bravos2k5.bravosshop.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void save(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    @Override
    public boolean saveIfAbsent(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    @Override
    public boolean saveIfAbsent(String key, Object value, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit));
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> List<T> multiGet(Collection<String> key) {
        return (List<T>) redisTemplate.opsForValue().multiGet(key);
    }

    @Override
    public <T> T getAndSet(String key, Object value) {
        return (T) redisTemplate.opsForValue().getAndSet(key,value);
    }


    @Override
    public <T> T getAndDelete(String key) {
        return (T) redisTemplate.opsForValue().getAndDelete(key);
    }

    @Override
    public <T extends Number> T increment(String key, long delta, Class<T> type) {
        return type.cast(redisTemplate.opsForValue().increment(key,delta));
    }

    @Override
    public <T extends Number> T decrement(String key, long delta, Class<T> type) {
        return type.cast(redisTemplate.opsForValue().decrement(key,delta));
    }

    @Override
    public <T extends Number> T increment(String key, double delta, Class<T> type) {
        return type.cast(redisTemplate.opsForValue().increment(key,delta));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void clearAll() {
        RedisConnectionFactory redisConnectionFactory = redisTemplate.getConnectionFactory();
        if (redisConnectionFactory != null && !redisConnectionFactory.getConnection().isClosed()) {
            redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        }
    }

}
