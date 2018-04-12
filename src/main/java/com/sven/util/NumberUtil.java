package com.sven.util;

/**
 * @author sven.CNang
 * @since 2018/4/11
 */
public class NumberUtil {
    private final static int ZERO_NUMBER = '0';
    private final static String[] CN = new String[] {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private final static String[] CN_DATE = new String[] {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private final static String[] UNIT = new String[] {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};

    /**
     * 数字装换为标准数字(只能转换)
     *
     * @param number
     * @return
     */
    public static String convert(int number) {
        String str = "";
        if (number < 0) {
            number = Math.abs(number);
            str = "负";
        }
        StringBuffer sb = new StringBuffer(String.valueOf(number));
        sb = sb.reverse();
        int r = 0;
        int l = 0;
        for (int j = 0; j < sb.length(); j++) {
            r = Integer.valueOf(sb.charAt(j) - ZERO_NUMBER);
            if (j == 0) {
                if (r != 0 || sb.length() == 1) {
                    str = CN[r];
                }
                continue;
            }
            if (j != 0) {
                l = Integer.valueOf(sb.charAt(j - 1) - ZERO_NUMBER);
            }
            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
                if (r != 0) {
                    str = CN[r] + UNIT[j] + str;
                } else if (l != 0) {
                    str = CN[r] + str;
                }
                continue;
            }
            if (j == 4 || j == 8) {
                str = UNIT[j] + str;
                if ((l != 0 && r == 0) || r != 0) {
                    str = CN[r] + str;
                }
                continue;
            }
        }
        if (9 < number && number < 20) {
            str = str.substring(1);
        }
        return str;
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

    public static void main(String[] args) {
        System.out.println(convert(1100000020));
    }
}
