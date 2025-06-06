package com.mariasorganics.farmtracker.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ExcelReportGenerator {

    public static void generateExcel(String sheetName, List<Map<String, Object>> data,
            HttpServletResponse response, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        if (!data.isEmpty()) {
            Row header = sheet.createRow(0);
            List<String> keys = new ArrayList<>(data.get(0).keySet());

            for (int i = 0; i < keys.size(); i++) {
                header.createCell(i).setCellValue(keys.get(i));
            }

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> rowData = data.get(i);
                for (int j = 0; j < keys.size(); j++) {
                    Object val = rowData.get(keys.get(j));
                    row.createCell(j).setCellValue(val != null ? val.toString() : "");
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public static void generateExcelFromMap(String sheetName, Map<String, ? extends Object> data,
        HttpServletResponse response, String fileName) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet(sheetName);

    int rowIndex = 0;
    for (Map.Entry<String, ? extends Object> entry : data.entrySet()) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(entry.getKey());
        row.createCell(1).setCellValue(entry.getValue() != null ? entry.getValue().toString() : "");
    }

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    workbook.write(response.getOutputStream());
    workbook.close();
}

}
