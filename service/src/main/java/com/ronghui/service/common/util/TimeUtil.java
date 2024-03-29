package com.ronghui.service.common.util;


import java.sql.Timestamp;

/**
 * @program: TimeUtil
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 14:26
 */
public class TimeUtil {

    public static Timestamp currentTimeStamp() {
        return new Timestamp(System.currentTimeMillis() + 8 * 60 * 60 * 1000);
    }
}