package com.sven.util.string;

import com.sven.util.StringUtil;
import org.junit.Test;

/**
 * Created by Administrator on 2017/4/20.
 */
public class StringUtilTest {
    @Test
    public void removeSpecialCharacter() {

        System.out.println(  StringUtil.isNumberAndLetter("w123"));
        System.out.println(StringUtil.formatNumber("123",3));
    }
}
