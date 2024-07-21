package org.kakaoshare.backend.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> T get(final String key, final Class<T> clazz) {
        final Object value = redisTemplate.opsForValue().get(key);
        if (value.getClass() != clazz) {
            throw new IllegalArgumentException();
        }

        return (T) value;
    }

    public <T> T remove(final String key, final Class<T> clazz) {
        final T value = get(key, clazz);
        redisTemplate.delete(key);
        return value;
    }
}
