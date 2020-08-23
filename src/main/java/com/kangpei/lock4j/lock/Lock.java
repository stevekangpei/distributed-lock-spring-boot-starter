package com.kangpei.lock4j.lock;

import com.kangpei.lock4j.LockInfo;

/**
 * description: LockExecutor
 * 顶层的LockExecutor接口，用于定义lock的核心方法<br>
 * date: 2020/8/22 9:20 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public interface Lock {


    boolean lock(String key, String value, long timeOut);

    boolean unLock(LockInfo lockInfo);

}
