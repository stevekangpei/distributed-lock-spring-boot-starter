package com.kangpei.lock4j;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description: LockInfo <br>
 * date: 2020/8/22 9:20 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
public class LockInfo {

    private String key;
    private String value;
    private long expire;
}
