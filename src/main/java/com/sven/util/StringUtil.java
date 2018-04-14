package com.sven.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 可以使用这个StringUtils。该类继承了apache-common包的字符工具类。
 *
 * @author sven
 */
public class StringUtil extends StringUtils {

    /**
     * 判断一个传入的字符串是否包含英文字母。 ps: 传入null认为不包含英文字母。
     *
     * @param word 传入的源字符串。
     * @return
     */
    public static boolean containLetter(String word) {
        if (word == null) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetter(word.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个传入的字符串是否全是英文 ps: 传入null返回false。
     *
     * @param word 传入的源字符串。
     * @return
     */
    public static boolean isEnglishWord(String word) {
        if (word == null) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetter(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个传入的字符串是数字 ps: 传入null返回false。
     *
     * @param word 传入的源字符串。
     * @return
     */
    public static boolean isNumber(String word) {
        if (isBlank(word)) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isDigit(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个传入的字符串是数字和字母的混合 ps: 传入null返回false。
     *
     * @param word 传入的源字符串。
     * @return
     */
    public static boolean isContainsNumberAndLetter(String word) {
        if (word == null) {
            return false;
        }

        Matcher m = Pattern.compile("^[\\da-zA-Z]+$").matcher(word);
        return m.find();
    }

    /**
     * 将_分开的字符串合并，并且首字母大写。如pure_benefit_contract，返回pureBenefitContract。
     *
     * @param str
     * @return
     */
    public static String getMergeString(String str) {
        if (StringUtil.isBlank(str)) {
            return "";
        }
        if (str.indexOf("_") == -1) {
            return str;
        }
        String[] pages = str.split("_");
        StringBuilder sb = new StringBuilder(pages[0]);
        for (int i = 1; i < pages.length; i++) {
            String p = pages[i];
            sb.append(convertFirstLetterToUpper(p));
        }
        return sb.toString();
    }

    /**
     * 将字符串首字母大写
     */
    public static String convertFirstLetterToUpper(String str) {
        if (StringUtil.isBlank(str)) {
            return "";
        }
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return new String(cs);
    }



    public static void main(String[] args) {

    }
}
