package com.kangpei.lock4j.autoconfigure;

import com.kangpei.lock4j.config.RedisProperties;
import com.kangpei.lock4j.lock.Lock;
import com.kangpei.lock4j.lock.LockExecutor;
import com.kangpei.lock4j.lock.LockTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

/**
 * description: LockAutoConfiguration <br>
 * date: 2020/8/22 10:47 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Configuration
@ConditionalOnClass(LockTemplate.class)
@EnableConfigurationProperties(RedisProperties.class)
public class LockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Jedis.class)
    public Jedis jedis(RedisProperties redisProperties) {
        return new Jedis(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate redisTemplate(RedisProperties redisProperties) {
        RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
        rsc.setPort(redisProperties.getPort());
        rsc.setHostName(redisProperties.getHost());

        RedisTemplate<String, String> template = new RedisTemplate<>();
        // 单机模式, 当前只实现单机模式
        JedisConnectionFactory fac = new JedisConnectionFactory(rsc);
        fac.afterPropertiesSet();
        template.setConnectionFactory(fac);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    @ConditionalOnMissingBean(Lock.class)
    public Lock lockExecutor() {
        Lock executor = new LockExecutor();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(LockTemplate.class)
    public LockTemplate lockTemplate() {
        LockTemplate template = new LockTemplate();
        return template;
    }

}
