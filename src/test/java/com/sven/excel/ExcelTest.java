package com.sven.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by sven on 2017/6/9.
 */
public class ExcelTest {
    @Test
    public void check() throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        CellStyle style = wb.createCellStyle();
        short n = 13;
        style.setFillBackgroundColor(n);
        Row row = sheet.createRow(0);
        row.setRowStyle(style);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        OutputStream os = new FileOutputStream("D:\\excel\\x.xlsx");
        wb.write(os);
        os.flush();
        os.close();
    }
}
