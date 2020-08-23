package com.kangpei.lock4j.utils;

import com.kangpei.lock4j.annotation.Lock;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * description: LockUtils <br>
 * date: 2020/8/22 10:14 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public final class LockUtil {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    public static String getLocalMac() {

        try {
            InetAddress host = InetAddress.getLocalHost();
            byte[] address = NetworkInterface.getByInetAddress(host).getHardwareAddress();
            StringBuffer builder = new StringBuffer();

            for (int i = 0; i < address.length; i++) {
                if (i != 0) {
                    builder.append("-");
                }
                String s = Integer.toHexString(address[i] & 0xFF);
                builder.append(s.length() == 1 ? 0 + s : s);
            }
            return builder.toString().toUpperCase().replaceAll("-", "");

        } catch (Exception e) {
            throw new IllegalArgumentException("getLocalMac address error");
        }
    }

    public static String getJvmPid() {

        String pid = ManagementFactory.getRuntimeMXBean().getName();
        int index = pid.indexOf("@");
        if (index > 0) {
            return pid.substring(0, index);
        }
        throw new IllegalStateException("get JvmPid error");
    }


    public static String generateKey(Method method) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(method.getDeclaringClass().getName()).append("-").append(method.getName());
        Lock lock = method.getAnnotation(Lock.class);
        if (lock.keys().length >= 1) {
            buffer.append(getSpelExpression(lock.keys(), method, method.getParameters()));
        }
        return buffer.toString();
    }

    private static String getSpelExpression(String[] keys, Method method, Parameter[] params) {
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(null, method, params, NAME_DISCOVERER);
        List<String> keysList = new ArrayList<>(keys.length);
        for (String key : keys) {
            if (!StringUtils.isEmpty(key)) {
                String s = PARSER.parseExpression(key).getValue(context).toString();
                keysList.add(s);
            }
        }
        return StringUtils.collectionToDelimitedString(keysList, ".", "", "");
    }

}
