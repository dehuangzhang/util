package com.sven.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间日期处理工具。
 *
 * @author yingchao.zyc
 */
@SuppressWarnings("deprecation")
public class DateFormatUtil {

    private static Logger logger = LoggerFactory.getLogger(DateFormatUtil.class);
    /**
     * 本地缓存
     */
    private static volatile Map<String, ThreadLocal<SimpleDateFormat>> FORMAT_LOCAL = new HashMap<>();

    private static final String FMT_YYMMDDHHMMSS = "yyyyMMddHHmmss";

    private static final String FMT_YYMMDD = "yyyyMMdd";

    private static final String FMT_WEEK = "EEEE";

    public static final String FMT_YYYY = "yyyy";

    public static final String FMT_Y_M_D = "yyyy-MM-dd";

    public static final String FMT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";

    private static final String CN_Y_M_D = "y年M月d日";

    private static final String CN_YY_MM_DD_HH_MM = "yyyy年MM月dd日 HH时mm分";

    /**
     * 双重锁校验
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getInstance(final String pattern) {
        ThreadLocal<SimpleDateFormat> local = FORMAT_LOCAL.get(pattern);
        if (local == null) {
            synchronized (DateFormatUtil.class) {
                local = FORMAT_LOCAL.get(pattern);
                if (local == null) {
                    local = new ThreadLocal<>();
                    local.set(new SimpleDateFormat(pattern));
                    FORMAT_LOCAL.put(pattern, local);
                }
            }
        }
        if (local.get() == null) {
            local.set(new SimpleDateFormat(pattern));
        }
        return local.get();
    }

    /**
     * 将字符串格式转换为日期格式
     *
     * @param pattern
     * @param dateStr
     * @return
     */
    private static Date parse(String dateStr, final String pattern) {
        try {
            return getInstance(pattern).parse(dateStr);
        } catch (Exception ex) {
            logger.error("getDateMin error, date = " + dateStr, ex);
        }
        return null;
    }

    /**
     * 将日期格式化成字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, final String pattern) {
        if (null == date) {
            return "";
        }
        return getInstance(pattern).format(date);
    }

    /**
     * 格式化 yy年MM月d日 H时mm分
     *
     * @return
     */
    public static String formatCNyyMMddHHmm(Date date) {
        return format(date, CN_YY_MM_DD_HH_MM);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return xxxx-xx-xx
     */
    public static String formateYMD(Date date) {
        return format(date, FMT_Y_M_D);
    }

    /**
     * 格式化制定的date:获取年份
     *
     * @param date
     * @return yyyy
     */
    public static String formateYyyy(Date date) {
        return format(date, FMT_YYYY);
    }

    /**
     * 格式化制定的datetime
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formateYyMmddHhMmSs(Date date) {
        return format(date, FMT_Y_M_D_H_M_S);
    }

    public static Date addDayFroDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    public static Date parseDate(String dateStr) {
        return parse(dateStr, FMT_Y_M_D);
    }

    /**
     * 日期格式化成xx年xx月xx日的格式
     *
     * @return
     */
    public static String formateCNYMD(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return year + "年" + month + "月" + day + "日";
    }

    /**
     * yyyyMMddHHmmss格式
     *
     * @param date
     * @return yyyyMMddHHmmss
     */
    public static String formateYyyyMMddHHmmss(Date date) {
        return format(date, FMT_YYMMDDHHMMSS);
    }

    public static Date parseDateTimeS(String dateStr) {
        return parse(dateStr, FMT_Y_M_D_H_M_S);
    }

    public static String ymdFormat(Date date) {
        return format(date, FMT_Y_M_D);
    }

    public static String ymdhmsFormatDate(Date date) {
        return format(date, FMT_Y_M_D_H_M_S);
    }

    /**
     * 返回指定日期是哪一年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 使用预设Format格式化Date成FMT_Y_M_D_H_M_S
     *
     * @param mils
     * @return
     */
    public static String format(long mils) {
        return format(new Date(mils), FMT_Y_M_D_H_M_S);
    }

    /**
     * 返回yyyy年MM月dd日 当月和日是个位数时不显示前面0
     *
     * @param date
     * @return
     */
    public static String getYMD(Date date) {
        return format(date, CN_Y_M_D);
    }

    /**
     * 返回MM月dd日
     *
     * @param date
     * @return
     */
    public static String getMMDD(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String dateStr = month + "月" + day + "日";

        return dateStr;
    }

    /**
     * 获取较小的日期
     */
    public static Date getMinDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return null;
        } else if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else {
            return date1.getTime() <= date2.getTime() ? date1 : date2;
        }
    }

    /**
     * 把英文日期转为汉字（例：二〇一六年十月二十八日）
     *
     * @param date
     * @return
     */
    public static String DateToChinese(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        StringBuilder sb = new StringBuilder();
        // 年编辑
        sb.append(NumberUtil.convertYear(calendar.get(Calendar.YEAR)));
        sb.append("年");
        sb.append(NumberUtil.convert(calendar.get(Calendar.MONTH) + 1));
        sb.append("月");
        sb.append(NumberUtil.convert(calendar.get(Calendar.DAY_OF_MONTH)));
        sb.append("日");
        return sb.toString();
    }

    /**
     * 解决生成PDF时，中文〇不显示的问题
     *
     * @param content
     */
    public static String replaceZero(String content) {
        return content.replaceAll("〇", "〇");
    }

    /**
     * 返回格式为：XXXX年XX月XX日XX时XX分 add by Felix 20160817
     *
     * @param date
     * @return
     */
    public static String getYearMonthDayNew(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String dateStr = year + "年" + month + "月" + day + "日" + hour + "时";
        if (minute > 0) {
            if (minute < 10) {
                dateStr += "0" + minute + "分";
            } else {
                dateStr += "" + minute + "分";
            }
        }
        return dateStr;
    }

    /**
     * 获取时间的时分秒
     *
     * @param date
     * @return
     */
    public static String getHHmmss(Date date) {
        if (null == date) {
            return "";
        }
        String formatDate = format(date, FMT_Y_M_D_H_M_S);
        String[] split = formatDate.split(" ");
        return split[1];
    }

    /**
     * 根据dateType获取一定时间内 日期列表 （入参时间精确到日）wb-lailinjia
     *
     * @param begin
     * @param end
     * @param dateType （月：Calendar.MONTH ； 日：Calendar.DAY_OF_MONTH）
     * @return
     * @throws ParseException
     */
    public static List<Date> findDates(String begin, String end, int dateType) {
        List<Date> lDate = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (dateType == Calendar.MONTH) {
            sdf = new SimpleDateFormat("yyyy-MM");
        }
        try {
            Date dBegin = sdf.parse(begin);
            Date dEnd = sdf.parse(end);

            lDate.add(dBegin);
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.setTime(dBegin);
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.setTime(dEnd);
            // 测试此日期是否在指定日期之后
            while (dEnd.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(dateType, 1);
                lDate.add(calBegin.getTime());
            }
        } catch (ParseException e) {
            logger.error("findDates error!" + String.format("begin: %s ,end: %s", begin, end));
        }
        return lDate;
    }

    /**
     * 当前时间是否在 start 和 end 之间
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean withinDateRange(Date start, Date end) {
        Date now = new Date();
        return now.after(start) && now.before(end);
    }

    /**
     * 获取日期的星期
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        return format(date, FMT_WEEK);
    }

    /**
     * 获取的当前时间的月
     *
     * @return
     */
    public static int getMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 某年的开始
     *
     * @param year
     * @return
     */

    public static Date getYearStartDate(int year) {
        String time = year + "-01-01 00:00:00";
        return parse(time, FMT_Y_M_D_H_M_S);
    }

    /**
     * 某年的结束
     *
     * @param year
     * @return
     */

    public static Date getYearEndDate(int year) {
        String time = year + "-12-31 23:59:59";
        return parse(time, FMT_Y_M_D_H_M_S);

    }

    /**
     * 获取上一月的开始时间
     *
     * @param year
     * @param month
     * @return
     */

    public static Date getLastMonthStartDate(int year, int month) {
        Calendar calender = Calendar.getInstance();

        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, month - 1 - 1);
        // 把日期设置为当月第一天
        calender.set(Calendar.DATE, 1);
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        Date monthStart = calender.getTime();
        return monthStart;
    }

    /**
     * 获取上一月的结束时间
     *
     * @param year
     * @param month
     * @return
     */

    public static Date getLastMonthEndDate(int year, int month) {
        Calendar calender = Calendar.getInstance();

        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, month - 1 - 1);

        // 把日期设置为当月第一天
        calender.set(Calendar.DATE, 1);
        // 日期回滚一天，也就是最后一天
        calender.roll(Calendar.DATE, -1);
        calender.set(Calendar.HOUR_OF_DAY, 23);
        calender.set(Calendar.MINUTE, 59);
        calender.set(Calendar.SECOND, 59);
        Date monthEnd = calender.getTime();
        return monthEnd;
    }

    /**
     * 获取某天的最小时间
     *
     * @param date
     * @return
     * @author wb-wyh281782 2018年1月23日 17:22:20
     */
    public static Date getDateMin(String date) {
        return parse(date + " 00:00:00", FMT_Y_M_D_H_M_S);
    }

    /**
     * 获取某天的最大时间
     *
     * @param date
     * @return
     * @author wb-wyh281782 2018年1月23日 17:22:35
     */
    public static Date getDateMax(String date) {
        return parse(date + " 23:59:59", FMT_Y_M_D_H_M_S);
    }

    public static String getDate(Date date) {
        return format(date, FMT_YYMMDD);
    }

    /**
     * 根据生日获取年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    public static void main(String[] args) {
        // System.out.println(DATETIME_FORMAT.format(getYearStartDate(2018)));
        String str
            = "{'janus': 'message','body': {'request': 'msg','room': 231,'msg': '{'type':'openTime',"
            + "'openTime':[{'type':'start','time':1473391260788},{'type':'stop','time':1473391287211},"
            + "{'type':'start','time':1473391289507}]}'},'transaction': 'eZN2gw94vkyx','token': "
            + "'gtihrZJeUx2nhwIoUgMiakfNXGqt5phN'}";
        JSONObject json = JSONObject.parseObject(str);
        JSONArray jsonArray = json.getJSONArray("openTime");
        System.out.println(jsonArray);
        // List<> a = jsonArray.toJavaList(CourtTimeVo);
        // String str = "2017-09-22";
        // String format2 = DATETIME_FORMAT.format(new Date());
        // String[] split = format2.split(" ");
        // str += " " + split[1];
        // try {
        // Date parse = DATETIME_FORMAT.parse(str);
        // // System.out.println(parse);
        // } catch (ParseException e) {
        // }

        // List<> a = jsonArray.toJavaList(CourtTimeVo);
        // String str = "2017-09-22";
        // String format2 = DATETIME_FORMAT.format(new Date());
        // String[] split = format2.split(" ");
        // str += " " + split[1];
        // try {
        // Date parse = DATETIME_FORMAT.parse(str);
        // // System.out.println(parse);
        // } catch (ParseException e) {
        // }

        // String nowDate = DateFormatUtil.formateNowDate(new Date());
    }

    /**
     * 返回传入日期 增加或减少整数天
     *
     * @param date
     * @param dayNum
     * @return
     */
    public static Date getDateAddDay(Date date, int dayNum) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, dayNum);
        return c.getTime();
    }

    /**
     * 返回两个日期间相差多少个小时
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getHoursBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0L;
        }
        return (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
    }

}
