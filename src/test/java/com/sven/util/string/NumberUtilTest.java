package com.sven.util.string;

import com.sven.util.NumberUtil;
import org.junit.Test;

/**
 * @author sven.zhang
 * @since 2018/4/13
 */
public class NumberUtilTest {
    @Test
    public void formatNumber() {
        System.out.println(NumberUtil.formatPrefixZero(100,7));
        System.out.println(NumberUtil.formatNumber(1023, 1, true));
    }
}
