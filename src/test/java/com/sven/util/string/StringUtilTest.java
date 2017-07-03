package com.sven.util.string;

import com.sven.util.StringUtil;
import org.junit.Test;

/**
 * Created by Administrator on 2017/4/20.
 */
public class StringUtilTest {
    @Test
    public void removeSpecialCharacter() {
        String str = "&*^$##%^&^&werw我的我的我的我的//*--//";
        str = StringUtil.removeSpecialCharacters(str);
        System.out.println(str);
    }
}
