package com.sven.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/20.
 */
public class StringUtil {
    private static final String regx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~" +
            "！@#￥%……&*（）——+|{}【】‘；：”“’。，、？-]";

    public static String removeSpecialCharacters(String str) {
        Pattern pattern = Pattern.compile(regx);
        Matcher m = pattern.matcher(str);
        return m.replaceAll("").trim();
    }
}
