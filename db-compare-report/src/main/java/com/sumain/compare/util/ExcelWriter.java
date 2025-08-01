package com.sumain.compare.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelWriter {

    public static void writeDiffToExcel(String filePath,
                                 List<String> keyFields,
                                 List<String> compareFields,
                                 List<Map<String, Object>> diffs) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Diff Report");

            // Header row
            Row header = sheet.createRow(0);
            int colIndex = 0;
            for (String k : keyFields) {
                header.createCell(colIndex++).setCellValue("KEY_" + k);
            }
            for (String c : compareFields) {
                header.createCell(colIndex++).setCellValue(c + "_SOURCE");
                header.createCell(colIndex++).setCellValue(c + "_TARGET");
            }

            // Style for diff highlight
            CellStyle highlightStyle = workbook.createCellStyle();
            highlightStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            highlightStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowIndex = 1;
            for (Map<String, Object> row : diffs) {
                Row dataRow = sheet.createRow(rowIndex++);
                colIndex = 0;

                // write key fields
                for (String k : keyFields) {
                    Object keyVal = row.get(k);
                    dataRow.createCell(colIndex++).setCellValue(keyVal != null ? keyVal.toString() : "");
                }

                // write compare fields
                for (String c : compareFields) {
                    Object sVal = row.get(c + "_SOURCE");
                    Object tVal = row.get(c + "_TARGET");
                    boolean isDiff = !String.valueOf(sVal).equals(String.valueOf(tVal));

                    Cell cell1 = dataRow.createCell(colIndex++);
                    cell1.setCellValue(sVal != null ? sVal.toString() : "");
                    if (isDiff) cell1.setCellStyle(highlightStyle);

                    Cell cell2 = dataRow.createCell(colIndex++);
                    cell2.setCellValue(tVal != null ? tVal.toString() : "");
                    if (isDiff) cell2.setCellStyle(highlightStyle);
                }
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }
}
