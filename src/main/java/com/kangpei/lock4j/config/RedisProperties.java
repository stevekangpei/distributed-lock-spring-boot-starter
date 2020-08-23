package com.kangpei.lock4j.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * description: RedisProperties <br>
 * date: 2020/8/23 7:44 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@ConfigurationProperties(prefix = "lock.redis")
@Data
public class RedisProperties {

    private String host;
    private Integer port;
}
