package com.kangpei.lock4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: Lock <br>
 * date: 2020/8/22 10:25 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Lock {

    /**
     * 默认的key名字为包名 + 方法名，如果用户自定义的话，可以加上用户自定义的可以
     * @return
     */
    String[] keys() default "";

    /**
     * 获取锁的超时时间
     * @return
     */
    long timeOut() default 3000;

    /**
     * 持有锁的超时时间，建议用户使用的时候设置好，需要大于业务的执行时间。
     * @return
     */
    long expire() default 3000;
}
