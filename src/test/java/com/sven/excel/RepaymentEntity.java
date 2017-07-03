package com.sven.excel;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sven on 2017/6/9.
 */
@Data
public class RepaymentEntity {
    private Date billDate;
    private BigDecimal interestPenalty;
    private BigDecimal interest;
    private BigDecimal principal;
    private BigDecimal overInterest;
    private BigDecimal principalPenalty;
}
