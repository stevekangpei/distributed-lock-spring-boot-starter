package com.kangpei.lock4j.lock;

import com.kangpei.lock4j.LockInfo;
import com.kangpei.lock4j.utils.LockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * description: LockTemplate
 * 真正对外提供加锁和解锁的实现类，
 * 类似于代理模式，代理了LockExecutor <br>
 * date: 2020/8/22 10:01 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Slf4j
public class LockTemplate {

    @Autowired
    private LockExecutor lockExecutor;

    public LockInfo lock(String key, long timeOut, long expire) throws InterruptedException {
        Assert.isTrue(timeOut > 0, "timeOut should be large than 0");
        String value = LockUtil.getLocalMac() + "_" + LockUtil.getJvmPid() + Thread.currentThread().getId();
        int acquireCount = 0;
        long cur = System.currentTimeMillis();
        while (System.currentTimeMillis() - cur < timeOut) {
            boolean lock = lockExecutor.lock(key, value, timeOut);
            acquireCount++;
            if (lock) return new LockInfo(key, value, expire);
            Thread.sleep(20);
        }
        log.info("try lock key {} {} times failed ", key, acquireCount);
        return null;
    }

    public boolean releaseLock(LockInfo info) {
        return lockExecutor.unLock(info);
    }

}
