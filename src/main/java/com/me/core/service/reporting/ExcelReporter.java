package com.me.core.service.reporting;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// row.createCell(currentPos); <-- if don't use setCellValue(), it'll be blank
// Setting Cell Properties: CellUtil.setCellStyleProperties(cell, properties);

public class ExcelReporter {
    private XSSFWorkbook wb;
    private CreationHelper createHelper;
    private Sheet currentSheet;
    private String outputFile;
    private int rowCounter;
    private int startIndex;

    ExcelReporter(String outputFile, String sheetName) {
        this.outputFile = outputFile;
        wb = new XSSFWorkbook();  // or new XSSFWorkbook();
        createHelper = wb.getCreationHelper();
        // make sure that name doesn't contain illegal symbols
        String safeName = WorkbookUtil.createSafeSheetName(sheetName);
        currentSheet = wb.createSheet(safeName);
        rowCounter = 0;
        startIndex = 0;
    }



    void setManualStartIndex(int index) {
        this.startIndex = index;
    }

    void resetStartIndex() {
        this.startIndex = 0;
    }

    private <T> Row addToRow(Row row, T val, int currentPos, boolean fill) {
        Cell cell = row.createCell(currentPos);

        if ((val instanceof Integer) ) {
            int parseInt = Integer.parseInt(val.toString());
            cell.setCellValue(parseInt);
        } else if (val instanceof Double) {
            double parseDouble = Double.parseDouble(val.toString());
            cell.setCellValue(parseDouble);
        } else if (val instanceof String) {
            cell.setCellValue(createHelper.createRichTextString(val.toString()));
        }

        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);

        if (fill) {
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        cell.setCellStyle(style);
        return row;
    }

    private <T> void addTable(boolean fill, List<List<T>> table) {
        table.forEach(tableRow -> addLine(fill, tableRow));
    }

    public <T> void addTable(List<List<T>> table) {
        addTable(false, table);
    }

    <T> void addLine(List<T> tableRow) {
        addLine(false, tableRow);
    }

    <T> void addLine(boolean fill, List<T> tableRow) {
        final int[] currentPos = {startIndex};
        final Row[] row = {currentSheet.createRow((short) rowCounter++)};

        tableRow.forEach(tableCell ->
                row[0] = addToRow(row[0], tableCell, currentPos[0]++, fill));

        for (int colNum = 0; colNum < row[0].getLastCellNum(); colNum++) {
            wb.getSheetAt(0).autoSizeColumn(colNum);
        }
    }

    void writeData() {
        try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
            wb.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}