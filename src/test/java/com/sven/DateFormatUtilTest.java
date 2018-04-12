package com.sven;

import java.util.Date;

import com.sven.util.DateFormatUtil;
import org.junit.Test;

/**
 * @author sven.zhang
 * @since 2018/4/11
 */
public class DateFormatUtilTest {
    @Test
    public void getFormatDate() {
        System.out.println(DateFormatUtil.DateToChinese(new Date()));
        System.out.println(DateFormatUtil.formatCNyyMMddHHmm(new Date()));
    }
}
