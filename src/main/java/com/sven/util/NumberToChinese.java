package com.sven.util;

/**
 * Created by sven on 2017/7/3.
 */
public class NumberToChinese {
    private final static String[] zh = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private final static String[] unit = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};

    /**
     * 数字装换为标准数字
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
            /**
             * 当前数字
             */
            r = Integer.valueOf(sb.substring(j, j + 1));

            if (j != 0)
            /**
             * 上一个数字
             */
                l = Integer.valueOf(sb.substring(j - 1, j));

            if (j == 0) {
                if (r != 0 || sb.length() == 1) str = zh[r];
                continue;
            }
            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
                if (r != 0) str = zh[r] + unit[j] + str;
                else if (l != 0) str = zh[r] + str;
                continue;
            }
            if (j == 4 || j == 8) {
                str = unit[j] + str;
                if ((l != 0 && r == 0) || r != 0) str = zh[r] + str;
                continue;
            }
        }
        if (9 < number && number < 20) {
            str = str.substring(1);
        }
        return str;
    }
}
