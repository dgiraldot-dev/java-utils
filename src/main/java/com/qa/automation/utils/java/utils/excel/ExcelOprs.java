package com.qa.automation.utils.java.utils.excel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.JavaException;
import org.apache.log4j.Level;
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

import static org.junit.Assert.assertTrue;

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
            new JavaException().catchException(e);
            return null;
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
            new JavaException().catchException(e);
        }
    }

    public void closeWorkbook() {
        try {
            if (fileInputStream != null) fileInputStream.close();
            if (fileOutputStream != null) fileOutputStream.close();
            if (workbook != null) workbook.close();
            resetAttributes();
        } catch (Exception e) {
            new JavaException().catchException(e);
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
            new JavaException().catchException(e);
        }
    }

    public boolean compareExcelFiles(String expectedFilePath, String actualFilePath) {

        StringOprs stringOprs = new StringOprs();

        LOGGER.log(Level.INFO, new StringBuilder("Comparando contenido de archivos excel: expectedFilePath: ").append(expectedFilePath).append(" | actualFilePath: ").append(actualFilePath).toString());

        JsonObject workbookJsonObject01 = loadWorkbookToJsonObject(expectedFilePath);
        JsonObject workbookJsonObject02 = loadWorkbookToJsonObject(actualFilePath);
        Object[] sheets01 = workbookJsonObject01.keySet().toArray();
        Object[] sheets02 = workbookJsonObject02.keySet().toArray();

        boolean sheetNumberValidation = true;
        boolean sheetNamesValidation = true;
        boolean rowNumberValidation = true;
        boolean columnNumberValidation = true;
        boolean cellValueValidation = true;
        boolean comparationStatus = true;

        // Number of sheets
        int numberOfSheets01 = sheets01.length;
        int numberOfSheets02 = sheets02.length;

        sheetNumberValidation = (numberOfSheets01 == numberOfSheets02);
        if (!sheetNumberValidation) {
            comparationStatus = false;
            sheetNamesValidation = false;
            rowNumberValidation = false;
            columnNumberValidation = false;
            cellValueValidation = false;
        }

        // Sheets names
        String sheetName01;
        String sheetName02;

        if (comparationStatus) {
            for (int i = 0; i < sheets01.length; i++) {
                sheetName01 = (String) sheets01[i];
                sheetName02 = (String) sheets02[i];
                if (sheetNamesValidation && (!sheetName01.equalsIgnoreCase(sheetName02))) {
                    comparationStatus = false;
                    sheetNamesValidation = false;
                    rowNumberValidation = false;
                    columnNumberValidation = false;
                    cellValueValidation = false;
                }
            }
        }

        // Number of rows
        int numberOfRows01;
        int numberOfRows02;

        if (comparationStatus) {
            for (int i = 0; i < sheets01.length; i++) {
                sheetName01 = (String) sheets01[i];
                sheetName02 = (String) sheets02[i];
                numberOfRows01 = workbookJsonObject01.get(sheetName01).getAsJsonArray().size();
                numberOfRows02 = workbookJsonObject02.get(sheetName02).getAsJsonArray().size();
                if (rowNumberValidation && (numberOfRows01 != numberOfRows02)) {
                    comparationStatus = false;
                    rowNumberValidation = false;
                    columnNumberValidation = false;
                    cellValueValidation = false;
                }
            }
        }

        // Number of columns
        int numberOfColumns01;
        int numberOfColumns02;

        if (comparationStatus) {
            for (int i = 0; i < sheets01.length; i++) {
                sheetName01 = (String) sheets01[i];
                sheetName02 = (String) sheets02[i];
                JsonArray sheet01 = workbookJsonObject01.get(sheetName01).getAsJsonArray();
                JsonArray sheet02 = workbookJsonObject02.get(sheetName02).getAsJsonArray();
                numberOfColumns01 = 0;
                numberOfColumns02 = 0;
                for (int j = 0; j < sheet01.size(); j++) {
                    int numberOfRowColumns01 = sheet01.get(j).getAsJsonArray().size();
                    int numberOfRowColumns02 = sheet02.get(j).getAsJsonArray().size();
                    if (numberOfRowColumns01 > numberOfColumns01) numberOfColumns01 = numberOfRowColumns01;
                    if (numberOfRowColumns02 > numberOfColumns02) numberOfColumns02 = numberOfRowColumns02;
                    if (columnNumberValidation && (numberOfColumns01 != numberOfColumns02)) {
                        comparationStatus = false;
                        columnNumberValidation = false;
                        cellValueValidation = false;
                    }
                }
            }
        }

        // Cell values
        if (comparationStatus) {
            for (int i = 0; i < sheets01.length; i++) {
                sheetName01 = (String) sheets01[i];
                sheetName02 = (String) sheets02[i];
                JsonArray sheet01 = workbookJsonObject01.get(sheetName01).getAsJsonArray();
                JsonArray sheet02 = workbookJsonObject02.get(sheetName02).getAsJsonArray();
                for (int j = 0; j < sheet01.size(); j++) {
                    JsonArray row01 = sheet01.get(j).getAsJsonArray();
                    JsonArray row02 = sheet02.get(j).getAsJsonArray();
                    for (int k = 0; k < row01.size(); k++) {
                        JsonObject cell01 = row01.get(k).getAsJsonObject();
                        JsonObject cell02 = row02.get(k).getAsJsonObject();
                        String cellValue01 = cell01.get(CELL_VALUE).getAsString();
                        String cellValue02 = cell02.get(CELL_VALUE).getAsString();
                        String cellFillColor01 = cell01.get(CELL_FILL_COLOR).getAsString();
                        if (((!cellValue01.equalsIgnoreCase(cellValue02)) && (stringOprs.isEmptyOrNull(cellFillColor01) || cellFillColor01.equalsIgnoreCase("FFFFFFFF"))) && (cellValueValidation)) {
                            comparationStatus = false;
                            cellValueValidation = false;
                        }
                    }
                }
            }
        }

        workbookJsonObject = null;

        LOGGER.log(Level.INFO, new StringBuilder("Resultado comparación contenido de archivos excel: ").append(comparationStatus).toString());

        LOGGER.log(Level.INFO, new StringBuilder("Resultado comparación contenido de archivos excel - Número de hojas: ").append(sheetNumberValidation).toString());
        LOGGER.log(Level.INFO, new StringBuilder("Resultado comparación contenido de archivos excel - Nombres y ubicación de hojas: ").append(sheetNamesValidation).toString());
        LOGGER.log(Level.INFO, new StringBuilder("Resultado comparación contenido de archivos excel - Número de filas en cada hoja: ").append(rowNumberValidation).toString());
        LOGGER.log(Level.INFO, new StringBuilder("Resultado comparación contenido de archivos excel - Número de columnas en cada hoja: ").append(columnNumberValidation).toString());
        LOGGER.log(Level.INFO, new StringBuilder("Resultado comparación contenido de archivos excel - Valores de celda en cada hoja: ").append(cellValueValidation).toString());

        return comparationStatus;
    }

    public void assertExcelFilesEquals(String expectedFilePath, String actualFilePath) {
        assertTrue("Assert Excel Files Equals", compareExcelFiles(expectedFilePath, actualFilePath));
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