package com.qa.automation.utils.java.utils.excel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.GenericRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ExcelOprs {

    private static final Logger LOGGER = LogManager.getLogger(ExcelOprs.class);
    private static final String CELL_VALUE = "cellValue";
    private static final String CELL_FILL_COLOR = "cellFillColor";
    private String filePath = null;
    private Workbook workbook = null;
    private JsonObject workbookJsonObject = null;
    private FileInputStream fileInputStream = null;
    private FileOutputStream fileOutputStream = null;
    private DataFormatter formatter = new DataFormatter();

    public ExcelOprs() {
        // Initialize without attributes
    }

    private void resetAttributes() {
        filePath = null;
        workbook = null;
        workbookJsonObject = null;
        fileInputStream = null;
        fileOutputStream = null;
    }

    public Workbook createWorkbook(String filePath) {
        this.filePath = filePath;
        workbook = new XSSFWorkbook();
        return workbook;
    }

    public Workbook loadWorkbook(String filePath) {
        this.filePath = filePath;
        try {
            fileInputStream = new FileInputStream(new File(filePath));
            workbook = new XSSFWorkbook(fileInputStream);
            return workbook;
        } catch (Exception e) {
            throw new GenericRuntimeException(e);
        }
    }

    public Sheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }

    public void saveWorkbook() {
        try {
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            closeWorkbook();
        } catch (Exception e) {
            throw new GenericRuntimeException(e);
        }
    }

    public void closeWorkbook() {
        try {
            if (fileInputStream != null) fileInputStream.close();
            if (fileOutputStream != null) fileOutputStream.close();
            if (workbook != null) workbook.close();
            resetAttributes();
        } catch (Exception e) {
            throw new GenericRuntimeException(e);
        }
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public String getFilePath() {
        return filePath;
    }


    public JsonObject getWorkbookJsonObject() {
        return workbookJsonObject;
    }

    public JsonArray getSheetJsonArray(String sheetName) {
        return workbookJsonObject.get(sheetName).getAsJsonArray();
    }

    public Sheet getSheet(String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public JsonArray getRowJsonArray(String sheetName, int rowNum) {
        return workbookJsonObject.get(sheetName).getAsJsonArray().get(rowNum).getAsJsonArray();
    }

    public Row getRow(String sheetName, int rowNum) {
        return workbook.getSheet(sheetName).getRow(rowNum);
    }

    public JsonObject getCellJsonObject(String sheetName, int rowNum, int cellNum) {
        return workbookJsonObject.get(sheetName).getAsJsonArray().get(rowNum).getAsJsonArray().get(cellNum).getAsJsonObject();
    }

    public String getCellPropertyJsonObject(String sheetName, int rowNum, int cellNum, String property) {
        return workbookJsonObject.get(sheetName).getAsJsonArray().get(rowNum).getAsJsonArray().get(cellNum).getAsJsonObject().get(property).getAsString();
    }

    public Cell getCell(String sheetName, int rowNum, int cellNum) {
        return workbook.getSheet(sheetName).getRow(rowNum).getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    public String getCellValueJsonElement(String sheetName, int rowNum, int cellNum) {
        return getCellPropertyJsonObject(sheetName, rowNum, cellNum, CELL_VALUE);
    }

    public String getCellValue(String sheetName, int rowNum, int cellNum) {
        return formatter.formatCellValue(getCell(sheetName, rowNum, cellNum));
    }

    public String getCellValue(String sheetName, int rowNum, int cellNum, FormulaEvaluator evaluator) {
        return formatter.formatCellValue(getCell(sheetName, rowNum, cellNum), evaluator);
    }

    public String getCellFillColorCodeJsonElement(String sheetName, int rowNum, int cellNum) {
        return getCellPropertyJsonObject(sheetName, rowNum, cellNum, CELL_FILL_COLOR);
    }

    public String getCellFillColorCode(String sheetName, int rowNum, int cellNum) {
        return getCellFillColorCode(getCell(sheetName, rowNum, cellNum));
    }

    public void setSheetWithCachedRowSet(CachedRowSet cachedRowSet, String sheetName) {
        setSheetWithCachedRowSet(cachedRowSet, sheetName, false, false);
    }

    public void setSheetWithCachedRowSet(CachedRowSet cachedRowSet, String sheetName, boolean autoFilter, boolean lockFirstRow) {
        ResultSetMetaData resultSetMetaData;
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) sheet = createSheet(sheetName);

        //>> Formatos de celda

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 8);
        font.setFontName("Calibri");

        Font fontBold = workbook.createFont();
        fontBold.setFontHeightInPoints((short) 8);
        fontBold.setFontName("Calibri");
        fontBold.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(fontBold);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFont(font);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        sheet.setDefaultRowHeight((short) 230);
        sheet.setDefaultColumnWidth(20);

        //<<

        try {
            int numberColumns = 0;
            resultSetMetaData = cachedRowSet.getMetaData();
            numberColumns = resultSetMetaData.getColumnCount();

            Row row;
            Cell cell;

            //>> Columnas

            row = sheet.createRow(0);

            for (int i = 0; i < numberColumns; i++) {
                cell = row.createCell(i);
                cell.setCellValue(resultSetMetaData.getColumnName(i + 1));
                cell.setCellStyle(headerStyle);
            }

            //<<

            //>> Registros

            int rowIndex = 1;
            cachedRowSet.beforeFirst();
            while (cachedRowSet.next()) {
                row = sheet.createRow(rowIndex);

                for (int i = 0; i < numberColumns; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(cachedRowSet.getString(i + 1));
                    cell.setCellStyle(dataStyle);
                }

                rowIndex = rowIndex + 1;
            }

            //<<

            if (lockFirstRow && (rowIndex > 1)) sheet.createFreezePane(0, 1);

            if (autoFilter && (rowIndex > 1)) sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, numberColumns - 1));

        } catch (SQLException e) {
            closeWorkbook();
        }
    }

    private boolean sheetNumberValidation = true;
    private boolean sheetNamesValidation = true;
    private boolean rowNumberValidation = true;
    private boolean columnNumberValidation = true;
    private boolean cellValueValidation = true;
    private boolean comparationStatus = true;

    private JsonObject workbookJsonObjectOne;
    private Object[] sheetsOne;
    String sheetNameOne;

    private JsonObject workbookJsonObjectTwo;
    private Object[] sheetsTwo;
    String sheetNameTwo;

    public boolean compareExcelFiles(String expectedFilePath, String actualFilePath) {
        LOGGER.info(new StringBuilder("Comparando contenido de archivos excel: expectedFilePath: ").append(expectedFilePath).append(" | actualFilePath: ").append(actualFilePath).toString());

        workbookJsonObjectOne = loadWorkbookToJsonObject(expectedFilePath);
        workbookJsonObjectTwo = loadWorkbookToJsonObject(actualFilePath);

        sheetsOne = workbookJsonObjectOne.keySet().toArray();
        sheetsTwo = workbookJsonObjectTwo.keySet().toArray();

        // Number of sheets
        validateNumberOfSheets(sheetsOne, sheetsTwo);

        // Sheets names
        validateSheetNames();

        // Number of rows
        validateNumberOfRows();

        // Number of columns
        validateNumberOfColumns();

        // Cell values
        validateCellValues();

        workbookJsonObject = null;

        LOGGER.info(new StringBuilder("Resultado comparación contenido de archivos excel: ").append(comparationStatus).toString());

        LOGGER.info(new StringBuilder("Resultado comparación contenido de archivos excel - Número de hojas: ").append(sheetNumberValidation).toString());
        LOGGER.info(new StringBuilder("Resultado comparación contenido de archivos excel - Nombres y ubicación de hojas: ").append(sheetNamesValidation).toString());
        LOGGER.info(new StringBuilder("Resultado comparación contenido de archivos excel - Número de filas en cada hoja: ").append(rowNumberValidation).toString());
        LOGGER.info(new StringBuilder("Resultado comparación contenido de archivos excel - Número de columnas en cada hoja: ").append(columnNumberValidation).toString());
        LOGGER.info(new StringBuilder("Resultado comparación contenido de archivos excel - Valores de celda en cada hoja: ").append(cellValueValidation).toString());

        return comparationStatus;
    }

    private void validateNumberOfSheets(Object[] sheetsOne, Object[] sheetsTwo) {
        int numberOfSheets01 = sheetsOne.length;
        int numberOfSheets02 = sheetsTwo.length;

        sheetNumberValidation = (numberOfSheets01 == numberOfSheets02);
        if (!sheetNumberValidation) {
            comparationStatus = false;
            sheetNamesValidation = false;
            rowNumberValidation = false;
            columnNumberValidation = false;
            cellValueValidation = false;
        }
    }

    private void validateSheetNames() {
        if (comparationStatus) {
            for (int i = 0; i < sheetsOne.length; i++) {
                sheetNameOne = (String) sheetsOne[i];
                sheetNameTwo = (String) sheetsTwo[i];
                if (sheetNamesValidation && (!sheetNameOne.equalsIgnoreCase(sheetNameTwo))) {
                    comparationStatus = false;
                    sheetNamesValidation = false;
                    rowNumberValidation = false;
                    columnNumberValidation = false;
                    cellValueValidation = false;
                }
            }
        }
    }

    private void validateNumberOfRows() {
        int numberOfRowsOne;
        int numberOfRowsTwo;

        if (comparationStatus) {
            for (int i = 0; i < sheetsOne.length; i++) {
                sheetNameOne = (String) sheetsOne[i];
                sheetNameTwo = (String) sheetsTwo[i];
                numberOfRowsOne = workbookJsonObjectOne.get(sheetNameOne).getAsJsonArray().size();
                numberOfRowsTwo = workbookJsonObjectTwo.get(sheetNameTwo).getAsJsonArray().size();
                if (rowNumberValidation && (numberOfRowsOne != numberOfRowsTwo)) {
                    comparationStatus = false;
                    rowNumberValidation = false;
                    columnNumberValidation = false;
                    cellValueValidation = false;
                }
            }
        }
    }

    private int numberOfColumnsOne = 0;
    private int numberOfColumnsTwo = 0;

    private void validateNumberOfColumns() {
        if (comparationStatus) {
            for (int i = 0; i < sheetsOne.length; i++) {
                sheetNameOne = (String) sheetsOne[i];
                sheetNameTwo = (String) sheetsTwo[i];
                JsonArray sheetOne = workbookJsonObjectOne.get(sheetNameOne).getAsJsonArray();
                JsonArray sheetTwo = workbookJsonObjectTwo.get(sheetNameTwo).getAsJsonArray();

                validateNumberOfColumnsInSheet(sheetOne, sheetTwo);
            }
        }
    }

    private void validateNumberOfColumnsInSheet(JsonArray sheetOne, JsonArray sheetTwo) {
        for (int j = 0; j < sheetOne.size(); j++) {
            int numberOfRowColumns01 = sheetOne.get(j).getAsJsonArray().size();
            int numberOfRowColumns02 = sheetTwo.get(j).getAsJsonArray().size();
            if (numberOfRowColumns01 > numberOfColumnsOne) numberOfColumnsOne = numberOfRowColumns01;
            if (numberOfRowColumns02 > numberOfColumnsTwo) numberOfColumnsTwo = numberOfRowColumns02;
            if (columnNumberValidation && (numberOfColumnsOne != numberOfColumnsTwo)) {
                comparationStatus = false;
                columnNumberValidation = false;
                cellValueValidation = false;
            }
        }
    }

    private void validateCellValues() {
        if (comparationStatus) {
            for (int i = 0; i < sheetsOne.length; i++) {
                sheetNameOne = (String) sheetsOne[i];
                sheetNameTwo = (String) sheetsTwo[i];
                JsonArray sheet01 = workbookJsonObjectOne.get(sheetNameOne).getAsJsonArray();
                JsonArray sheet02 = workbookJsonObjectTwo.get(sheetNameTwo).getAsJsonArray();
                for (int j = 0; j < sheet01.size(); j++) {
                    validateCellValuesInSheet(sheet01.get(j).getAsJsonArray(), sheet02.get(j).getAsJsonArray());
                }
            }
        }
    }

    private void validateCellValuesInSheet(JsonArray rowOne, JsonArray rowTwo) {
        for (int k = 0; k < rowOne.size(); k++) {
            JsonObject cell01 = rowOne.get(k).getAsJsonObject();
            JsonObject cell02 = rowTwo.get(k).getAsJsonObject();
            String cellValue01 = cell01.get(CELL_VALUE).getAsString();
            String cellValue02 = cell02.get(CELL_VALUE).getAsString();
            String cellFillColor01 = cell01.get(CELL_FILL_COLOR).getAsString();
            if (((!cellValue01.equalsIgnoreCase(cellValue02)) && (new StringOprs().isEmptyOrNull(cellFillColor01) || cellFillColor01.equalsIgnoreCase("FFFFFFFF"))) && (cellValueValidation)) {
                comparationStatus = false;
                cellValueValidation = false;
            }
        }
    }

    public JsonObject loadWorkbookToJsonObject(String filePath) {
        JsonObject tempWorkbookJsonObject = new JsonObject();
        loadWorkbook(filePath);

        int numberOfSheets = workbook.getNumberOfSheets();
        int numberOfRows = 0;
        int numberOfColumns = 0;
        Sheet sheet;
        String sheetName;
        Row row;
        Cell cell = null;
        JsonObject cellJsonObject;

        for (int sh = 0; sh < numberOfSheets; sh++) {
            sheet = workbook.getSheetAt(sh);
            sheetName = sheet.getSheetName();
            tempWorkbookJsonObject.add(sheetName, new JsonArray(numberOfSheets));

            numberOfRows = sheet.getLastRowNum() + 1;

            if (sheet.getPhysicalNumberOfRows() > 0) {
                for (int ro = 0; ro < numberOfRows; ro++) {
                    row = sheet.getRow(ro);
                    try {
                        numberOfColumns = row.getLastCellNum();
                    } catch (Exception e) {
                        numberOfColumns = 0;
                    }

                    tempWorkbookJsonObject.get(sheetName).getAsJsonArray().add(new JsonArray(numberOfRows));

                    for (int co = 0; co < numberOfColumns; co++) {
                        cellJsonObject = new JsonObject();
                        cell = row.getCell(co, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        cellJsonObject.addProperty(CELL_VALUE, formatter.formatCellValue(cell));
                        cellJsonObject.addProperty(CELL_FILL_COLOR, getCellFillColorCode(cell));
                        tempWorkbookJsonObject.get(sheetName).getAsJsonArray().get(ro).getAsJsonArray().add(cellJsonObject);
                    }
                }
            }
        }

        closeWorkbook();

        return tempWorkbookJsonObject;
    }

    public String getCellFillColorCode(Cell cell) {
        String colorCode;

        try {
            colorCode = ((ExtendedColor) cell.getCellStyle().getFillForegroundColorColor()).getARGBHex();
        } catch (Exception e) {
            colorCode = "";
        }

        return colorCode;
    }
}