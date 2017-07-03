package com.sven.thread;

import com.sven.model.RepaymentEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sven
 *         Created by wb-zdh274635 on 2017/6/5.
 */
public class ExcelOperate {

    private static final List<String> REPAYMENT_TYPE_LIST = Arrays.asList("后台批量还款", "批扣还款", "动账通知还款");
    private static final List<String> LOAN_LIST = Arrays.asList("放款");
    private static final String END_DATE = "20170607";
    private List<SimpleDateFormat> formats = Arrays.asList(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
        new SimpleDateFormat("yyyyMMdd"), new SimpleDateFormat("yyyy/MM/dd"));

    /**
     * 1.获取借呗本金
     * 2.每月利息
     *
     * @param is
     * @param path
     * @throws IOException
     */
    public void operate(InputStream is, String path) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook(is);
        Sheet sheet = wb.getSheetAt(0);
      /*  CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(
            wb.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss")
        );*/

        Row first = sheet.getRow(0);
        first.createCell(9).setCellValue("24%利息金额");
        first.createCell(10).setCellValue("36%利息金额");
        first.createCell(11).setCellValue("当前利息金额");
        first.createCell(12).setCellValue("应还利息总金额");
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        //剩余本金
        BigDecimal leftPrincipal = BigDecimal.ZERO;
        Calendar firstCal = Calendar.getInstance();
        Calendar secCal = Calendar.getInstance();
        //第一期本金
        BigDecimal firstPrincipal = BigDecimal.ZERO;
        BigDecimal interset24 = BigDecimal.ZERO;
        BigDecimal interset36 = BigDecimal.ZERO;
        BigDecimal percent24 = new BigDecimal("0.02");
        BigDecimal percent36 = new BigDecimal("0.03");
        BigDecimal basicPrincipal = BigDecimal.ZERO;
        BigDecimal remainPrincipal;
        RepaymentEntity repay = new RepaymentEntity();
        int num = sheet.getLastRowNum();
        for (int i = 1; i <= num; i++) {
            Row row = sheet.getRow(i);
            String type = row.getCell(0).getStringCellValue();
            Date dealDate = translateDate(row.getCell(1));
            cal2.setTime(dealDate);
            if (LOAN_LIST.contains(type)) {
                cal.setTime(dealDate);
                firstCal.setTime(dealDate);
                firstCal.add(Calendar.MONTH, 1);
                secCal.setTime(dealDate);
                secCal.add(Calendar.MONTH, 2);
                basicPrincipal = translate(row.getCell(2));
                leftPrincipal = basicPrincipal;
                continue;
            }
            BigDecimal p24 = BigDecimal.ZERO;
            BigDecimal p36 = BigDecimal.ZERO;
            BigDecimal principal = translate(row.getCell(3));
            BigDecimal interest = translate(row.getCell(4));
            BigDecimal overPrincipal = translate(row.getCell(5));
            BigDecimal overInterest = translate(row.getCell(6));
            BigDecimal principalPenalty = translate(row.getCell(7));
            BigDecimal interestPenalty = translate(row.getCell(8));
            BigDecimal totalInterest = interest.add(interestPenalty).add(principalPenalty).add(overInterest);
            if ("应还".equals(type)) {
                repay.setBillDate(dealDate);
                repay.setInterestPenalty(interestPenalty);
                repay.setInterest(interest);
                repay.setOverInterest(overInterest);
                repay.setPrincipal(principal);
                repay.setPrincipalPenalty(principalPenalty);
                remainPrincipal = principal.add(overPrincipal);
                BigDecimal months = calMonths(cal2, format(END_DATE));
                p24 = percent24.multiply(months).multiply(remainPrincipal);
                p36 = percent36.multiply(months).multiply(remainPrincipal);
                BigDecimal sum = totalInterest;
                if (totalInterest.compareTo(p24) == 1) {
                    sum = p24;
                }
                row.createCell(12).setCellValue(sum.toString());

            }
            if (REPAYMENT_TYPE_LIST.contains(type) || type.contains("还款")) {
                if (cal2.compareTo(firstCal) < 1) {
                    firstPrincipal = firstPrincipal.add(principal);
                }
                if (cal2.compareTo(firstCal) >= 1 && cal2.compareTo(secCal) < 1) {
                    firstPrincipal = firstPrincipal.add(overPrincipal);
                }
                if (firstPrincipal.compareTo(BigDecimal.ZERO) == 0) {
                    throw new RuntimeException("非等额本息模式，无法计算期数");
                }
                BigDecimal year = calYear(cal, cal2);
                p24 = percent24.multiply(year).multiply(leftPrincipal);
                p36 = percent36.multiply(year).multiply(leftPrincipal);
                if (calDays(cal, cal2).compareTo(BigDecimal.ZERO) == 0) {
                    p24 = interset24;
                    p36 = interset36;
                }
                interset24 = p24;
                interset36 = p36;
                leftPrincipal = leftPrincipal.subtract(principal.add(overPrincipal));
                cal.setTime(cal2.getTime());
            }
            row.createCell(9).setCellValue(p24.setScale(2, BigDecimal.ROUND_DOWN).toString());
            row.createCell(10).setCellValue(p36.setScale(2, BigDecimal.ROUND_DOWN).toString());
            row.createCell(11).setCellValue(totalInterest.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }
        //创建未完成的账单
        //总共期数
        BigDecimal number = basicPrincipal.divide(firstPrincipal, 0, BigDecimal.ROUND_DOWN);
        //剩余期数
        BigDecimal remainNumber = leftPrincipal.divide(firstPrincipal, 0, BigDecimal.ROUND_DOWN);
        Calendar start = Calendar.getInstance();
        start.setTime(repay.getBillDate());
        BigDecimal count = calMonths(start, Calendar.getInstance()).subtract(number.subtract(remainNumber));
        BigDecimal averageInterset = repay.getInterest().divide(count, 2, BigDecimal.ROUND_DOWN);
        BigDecimal averagePrincipalPenalty = repay.getPrincipalPenalty().divide(count, 2, BigDecimal.ROUND_DOWN);
        BigDecimal averageInterestPenalty = repay.getInterestPenalty().divide(count, 2, BigDecimal.ROUND_DOWN);
        BigDecimal overInterest = BigDecimal.ZERO;
        BigDecimal principal = firstPrincipal;
        firstCal.add(Calendar.MONTH, number.subtract(remainNumber).intValue() - 1);
        firstCal.add(Calendar.DAY_OF_MONTH, -1);
        BigDecimal overPrincipal = BigDecimal.ZERO;
        BigDecimal principalPenalty = BigDecimal.ZERO;
        BigDecimal interestPenalty = BigDecimal.ZERO;
        BigDecimal p24 = BigDecimal.ZERO;
        BigDecimal p36 = BigDecimal.ZERO;
        int last = sheet.getLastRowNum() + 1;
        int n = 0, total = count.intValue() - 1;
        int re = remainNumber.intValue() - 1;
        boolean flag = false;
        XSSFFont font = wb.createFont();
        font.setColor(XSSFFont.COLOR_RED);
        XSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        for (int i = 0; i <= total; i++) {
            firstCal.add(Calendar.MONTH, 1);
            if (i == total) {
                firstCal.setTime(format(END_DATE).getTime());
            }
            Row row = sheet.createRow(last + i);
            row.setRowStyle(style);
            row.createCell(0).setCellValue("本期应还");
            String date = format(firstCal.getTime());
            row.createCell(1).setCellValue(date);
            if (n == re && !flag) {
                principal = leftPrincipal.subtract(firstPrincipal.multiply(remainNumber.subtract(BigDecimal.ONE)));
                flag = true;
            }
            principalPenalty = principalPenalty.add(averagePrincipalPenalty);
            interestPenalty = interestPenalty.add(averageInterestPenalty);
            if (i == total) {
                averageInterset = repay.getInterest().subtract(averageInterset.multiply(new BigDecimal(total)));
                averageInterestPenalty = repay.getInterestPenalty().subtract(
                    averageInterestPenalty.multiply(new BigDecimal(total)));
                principalPenalty = repay.getPrincipalPenalty();
                interestPenalty = repay.getInterestPenalty();
            }
            BigDecimal intersets = interestPenalty.add(principalPenalty).add(overInterest).add(averageInterset);
            row.createCell(2).setCellValue(intersets.add(overPrincipal).add(principal).toString());
            row.createCell(3).setCellValue(principal.toString());
            row.createCell(4).setCellValue(averageInterset.toString());
            row.createCell(5).setCellValue(overPrincipal.toString());
            row.createCell(6).setCellValue(overInterest.toString());
            row.createCell(7).setCellValue(principalPenalty.toString());
            row.createCell(8).setCellValue(interestPenalty.toString());
            BigDecimal year = calYear(start, firstCal);
            p24 = percent24.multiply(year).multiply(leftPrincipal).add(p24);
            p36 = percent36.multiply(year).multiply(leftPrincipal).add(p36);
            row.createCell(9).setCellValue(p24.setScale(2, BigDecimal.ROUND_DOWN).toString());
            row.createCell(10).setCellValue(p36.setScale(2, BigDecimal.ROUND_DOWN).toString());
            row.createCell(11).setCellValue(intersets.toString());
            overInterest = overInterest.add(averageInterset);
            overPrincipal = overPrincipal.add(principal);
            if (n <= re) {
                n++;
            }
            if (flag) {
                principal = BigDecimal.ZERO;
            }
        }
        OutputStream os = new FileOutputStream(path);
        is.close();
        wb.write(os);
        os.flush();
        os.close();
    }

    private BigDecimal calYear(Calendar start, Calendar bilDate) {
        long days = (bilDate.getTimeInMillis() - start.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        int month = bilDate.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int year = bilDate.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        if (month == 0 && year == 0 && days > 0) {
            month += 1;
        }
        BigDecimal total = new BigDecimal(year * 12 + month).divide(new BigDecimal(12), 0, BigDecimal.ROUND_UP);
        return total;
    }

    private BigDecimal calMonths(Calendar start, Calendar bilDate) {
        int month = bilDate.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int year = bilDate.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        return new BigDecimal(year * 12 + month);
    }

    private BigDecimal calDays(Calendar start, Calendar bilDate) {
        long days = (bilDate.getTimeInMillis() - start.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        return new BigDecimal(days);
    }

    /**
     * excel 数字类型转化为BigDecimal
     *
     * @param cell
     * @return
     */
    private BigDecimal translate(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        int type = cell.getCellType();
        String result = "0.00";
        switch (type) {
            case Cell.CELL_TYPE_STRING:
                result = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                result = cell.getNumericCellValue() + "";
                break;
            default:
        }
        result = result.replaceAll(",", "");
        return new BigDecimal(result);
    }

    private Date translateDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        int type = cell.getCellType();
        String result = "";
        switch (type) {
            case Cell.CELL_TYPE_STRING:
                result = cell.getStringCellValue();
                break;
            default:
                return cell.getDateCellValue();
        }
        return format(result).getTime();
    }

    /**
     * 时间转化
     *
     * @param date
     * @return
     */
    private Calendar format(String date) {
        Calendar calendar = Calendar.getInstance();

        Date d = null;
        for (SimpleDateFormat format : formats) {
            try {
                d = format.parse(date);
            } catch (Exception e) {
                continue;
            }
            break;
        }
        if (d == null) {
            throw new RuntimeException("日期格式不正确");
        }
        calendar.setTime(d);
        return calendar;
    }

    private String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }

    private void read(File file, String outPath) {
        try {
            InputStream is = new FileInputStream(file);
            new ExcelOperate().operate(is, outPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败文件名" + file.getName());
        }
    }

    public void readFile(String dir, String outDir) {
        File file = new File(dir);
        if (file.isFile()) {
            read(file, outDir);
            return;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            int nThreads = files.length / 10 + 1;
            ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>());
            for (int i = 0; i < files.length; i++) {
                File f = files[i];

                String path = outDir + "\\" + f.getName();
                executor.execute(() -> {
                    read(f, path);
                });
            }
            executor.shutdown();
        }
    }

    /**
     * 删除文件夹下所有文件
     *
     * @param file
     */
    private static void delete(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                delete(f);
            }
            f.delete();
        }

    }

    public static void main(String[] args) throws Exception {
        String dirtory = "D:\\Documents";
        String out = "D:\\excel";
        File file = new File(out);
        if (file.exists()) {
            delete(file);
        } else {
            file.mkdirs();
        }
        new ExcelOperate().readFile(dirtory, out);
    }

}

