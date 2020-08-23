package com.kangpei.lock4j.config;

import com.kangpei.lock4j.lock.Lock;
import com.kangpei.lock4j.lock.LockExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * description: LockConfig <br>
 * date: 2020/8/22 9:31 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Configuration
public class LockConfig {

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        return redisTemplate;
    }

    @Bean
    public Lock lockExecutor() {
        Lock executor = new LockExecutor();
        return executor;
    }
}
