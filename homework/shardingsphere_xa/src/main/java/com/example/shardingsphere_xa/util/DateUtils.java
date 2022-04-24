package com.example.shardingsphere_xa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/16
 */
public class DateUtils {

    public static Integer getDataNumber() {
        return getDataNumber("yyyyMMdd", null);
    }

    public static Integer getDataNumber(String pattern, String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String now = dateFormat.format(null != dateStr ? dateStr : new Date());
        return Integer.parseInt(now);
    }
}
