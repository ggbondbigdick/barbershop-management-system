package com.barber.tool;

import cn.hutool.core.util.StrUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * ClassName: DateTool
 * Description: 时间计算
 */
public class DateTool {

    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate() { return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());}


    /**
     * 获取日期时间戳
     * @param localDate
     * @return
     */
    public static long getDateTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }


    /**
     * 获取时间戳
     * @param localDateTime
     * @return
     */
    public static long getTimeTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }


    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() { return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());}


    /**
     * 获取当前时区时间
     * @param timeZone
     * @return
     */
    public static String getCurrentZoneTime(String timeZone) {
        ZoneId zoneId = ZoneId.of(timeZone);
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now(zoneId));
    }


    /**
     * 将字符串日期转换为 LocalDate 类型
     * @param date
     */
    public static LocalDate getLocalDate(String date) {

        if (StrUtil.isBlank(date)) {
            return null;
        }

        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    /**
     * 将字符串日期转换为 LocalDateTime 类型
     * @param date
     * @return
     */
    public static LocalDateTime getLocalDateTime(String date) {

        if (StrUtil.isBlank(date)) {
            return null;
        }

        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 获取当前日期之后 days 天的日期
     * @param days
     * @return
     */
    public static String getCurrentDateTheAfterDate(int days) {

        // 获取当前日期
        LocalDate now = LocalDate.now();

        // 加上 day 天
        LocalDate localDate = now.plusDays(days);

        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate);
    }


    /**
     * 获取月份的最后一天
     * @param currentMonth 当前月份 2000-01-01
     * @return
     */
    public static int getLastDayOfMonth(String currentMonth) {
        try {
            YearMonth yearMonth = YearMonth.parse(currentMonth, DateTimeFormatter.ISO_LOCAL_DATE);
            return yearMonth.atDay(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        } catch (Exception e) {
            System.err.println("无效的日期格式: " + currentMonth);
            return -1;
        }
    }
}
