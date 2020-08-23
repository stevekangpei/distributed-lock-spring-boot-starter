package com.kangpei.lock4j.lock;

import com.kangpei.lock4j.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

/**
 * description: LockExecutor <br>
 * date: 2020/8/22 9:21 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Slf4j
public class LockExecutor implements Lock{

    private static final RedisScript<String> SCRIPT_LOCK =
            new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1], " +
                    "'NX', 'PX', ARGV[2])", String.class);
    private static final RedisScript<String> SCRIPT_UNLOCK =
            new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] " +
                    "then return tostring(redis.call('del', KEYS[1])==1) " +
                    "else return 'false' end", String.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean lock(String key, String value, long timeOut) {
        Object o = redisTemplate.execute(SCRIPT_LOCK, redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(), Collections.singletonList(key),
                value, String.valueOf(timeOut));
        return "OK".equalsIgnoreCase(o.toString());
    }

    public boolean unLock(LockInfo lockInfo) {

        Object o = redisTemplate.execute(SCRIPT_UNLOCK, redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(), Collections.singletonList(lockInfo.getKey()),
                lockInfo.getValue());
        return Boolean.parseBoolean(o.toString());
    }
}
