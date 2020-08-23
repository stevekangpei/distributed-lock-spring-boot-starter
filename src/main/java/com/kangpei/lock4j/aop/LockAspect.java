package com.kangpei.lock4j.aop;

import com.kangpei.lock4j.LockInfo;
import com.kangpei.lock4j.annotation.Lock;
import com.kangpei.lock4j.lock.LockTemplate;
import com.kangpei.lock4j.utils.LockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * description: LockAspect <br>
 * date: 2020/8/22 10:29 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Aspect
@Component
@Slf4j
public class LockAspect {

    @Autowired
    private LockTemplate lockTemplate;

    @Pointcut("annotation(com.kangpei.lock4j.annotation.Lock)")
    public void lock() {

    }

    @Around("lock()")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String key = LockUtil.generateKey(method);
        LockInfo info = null;
        try {
            info = lockTemplate.lock(key, lock.timeOut(), lock.expire());
            if (null != info) {
                return joinPoint.proceed();
            }
        } catch (Exception e) {
            log.error("invoke method {} error", method.getName(), e);
        } finally {
            if (null != info) {
                lockTemplate.releaseLock(info);
            }
        }
        return null;
    }
}
