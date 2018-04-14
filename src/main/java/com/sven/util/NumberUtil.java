package com.sven.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author sven.CNang
 * @since 2018/4/11
 */
public class NumberUtil {
    private final static int ZERO_NUMBER = '0';
    private final static int TEN = 10;
    private final static String[] CN = new String[] {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private final static String[] CN_DATE = new String[] {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private final static String[] UNIT = new String[] {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};

    /**
     * 数字装换为标准数字(只能转换)
     * (有问题)
     *
     * @param number
     * @return
     */
    public static String convert(long number) {
        StringBuilder sb = new StringBuilder();
        if (number < 0) {
            number = Math.abs(number);
            sb.append("负");
        }
        String n = String.valueOf(number);
        int r, l = n.length();
        for (int j = 0; j < n.length(); j++) {
            r = Integer.valueOf(n.charAt(j) - ZERO_NUMBER);
            sb.append(CN[r]);
            if (CN[0].equals(CN[r])) {
                continue;
            }
            int left = l - j;
            if (left == 8) {
                sb.append(UNIT[left - 8]);
            }
            if (left == 4) {
                sb.append(UNIT[left - 4]);
            }
            sb.append(UNIT[left - 1]);
        }
        String result = sb.toString();
        if (result.endsWith("零")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 转年份
     *
     * @param number
     * @return
     */
    public static String convertYear(int number) {
        String numberStr = String.valueOf(number);
        StringBuilder builder = new StringBuilder();
        int length = numberStr.length();
        for (int i = 0; i < length; i++) {
            int s = numberStr.charAt(i) - ZERO_NUMBER;
            builder.append(CN_DATE[s]);
        }
        return builder.toString();
    }

    /**
     * 不足左补零
     *
     * @param length 长度
     * @param number 数字
     * @return
     */
    public static String formatPrefixZero(Integer number, int length) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }

    /**
     * 数字转字符串（不使用科学计数法、去除小数点后无用的0）
     *
     * @param param
     * @return
     */
    public static String formatAmount(BigDecimal param) {
        if (null == param) {
            return "0";
        }
        BigDecimal bigDecimal = param.stripTrailingZeros();
        return bigDecimal.toPlainString();
    }

    /**
     * 数字格式化
     *
     * @param obj         浮点型数字
     * @param digit       小数精度
     * @param isThousands 是否需要千分位
     * @return
     */
    public static String formatNumber(Object obj, int digit, boolean isThousands) {
        if (obj == null) {
            return "";
        }
        String format = "##";
        if (isThousands) {
            format = "###,###";
        }
        if (digit > 0) {
            format += ".00";
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        String result = decimalFormat.format(obj);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(convert(1000000001));
    }
}
