/**
 * Grupo Aval Acciones y Valores S.A. CONFIDENTIAL
 *
 * <p>Copyright (c) 2018 . All Rights Reserved.
 *
 * <p>NOTICE: This file is subject to the terms and conditions defined in file 'LICENSE', which is
 * part of this source code package.
 */
package com.qa.automation.utils.java.utils.data_source;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.GenericRuntimeException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class DataSource {

    private String filePath;
    private String directoryPath;
    private String fileName;
    private String fileExtension;
    private String fileCopyPath;
    private String sheetName;

    private ArrayList<String> filteredColumnNameList = new ArrayList<>();
    private ArrayList<String> fullColumnNameList = new ArrayList<>();
    private ArrayList<String> fullPropertyList = new ArrayList<>();
    private ArrayList<String> filteredPropertyList = new ArrayList<>();
    private JsonObject filteredDataValues = new JsonObject();
    private JsonObject fullDataValues = new JsonObject();
    private JsonObject dataAddresses = new JsonObject();
    private Object[] filteredKeyList;
    private Object[] fullKeyList;

    private DataFormatter dataFormatter = new DataFormatter();
    private Workbook workbook = null;
    private JsonObject filteredDataValueJsonObject;
    private JsonObject fullDataValueJsonObject;
    private JsonObject dataAddressJsonObject;
    private JsonArray cellAddressJsonArray;

    public DataSource(String filePath, String sheetName) {
        FileOprs fileOprs = new FileOprs();

        tryFindDataSourceFileAndReturnPath(filePath);
        this.directoryPath = fileOprs.getDirectoryPathFromFilePath(filePath);
        this.fileName = fileOprs.getFileNameWithoutExtensionFromFilePath(filePath);
        this.fileExtension = fileOprs.getFileExtensionFromFilePath(filePath);
        this.fileCopyPath = new StringBuilder(directoryPath).append(fileName).append("_copy").append(".").append(fileExtension).toString();
        this.sheetName = sheetName;
        loadData();
        if (filteredDataValues.size() > 0) filteredKeyList = filteredDataValues.keySet().toArray();
        if (fullDataValues.size() > 0) fullKeyList = fullDataValues.keySet().toArray();
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getFileCopyPath() {
        return fileCopyPath;
    }

    public void createFileCopy() {
        new FileOprs().copyFile(filePath, fileCopyPath);
    }

    public List<String> getFilteredColumnNameList() {
        return filteredColumnNameList;
    }

    public List<String> getFullColumnNameList() {
        return fullColumnNameList;
    }

    public List<String> getFilteredPropertyList() {
        return filteredPropertyList;
    }

    public List<String> getFullPropertyList() {
        return fullPropertyList;
    }

    public JsonObject getFilteredDataValues() {
        return filteredDataValues;
    }

    public JsonObject getFullDataValues() {
        return fullDataValues;
    }

    public JsonObject getDataAddresses() {
        return dataAddresses;
    }

    public JsonArray getDataAddress(String key, String property) {
        return dataAddresses.get(key).getAsJsonObject().get(property).getAsJsonArray();
    }

    public List<Object> getFilteredKeyList() {
        return Arrays.asList(filteredKeyList);
    }

    public List<Object> getFullKeyList() {
        return Arrays.asList(fullKeyList);
    }

    public int getDataRowIndex(String key, String property) {
        return getDataAddress(key, property).get(0).getAsInt();
    }

    public int getDataColumnIndex(String key, String property) {
        return getDataAddress(key, property).get(1).getAsInt();
    }

    public JsonObject getDataItemFromFilteredDataValues(String key) {
        return filteredDataValues.getAsJsonObject(key);
    }

    public JsonObject getDataItemFromFullDataValues(String key) {
        return fullDataValues.getAsJsonObject(key);
    }

    public Cell getCell(int rowIndex, int columnIndex) {
        return (workbook != null) ? workbook.getSheet(sheetName).getRow(rowIndex).getCell(columnIndex) : null;
    }

    public Cell getCell(String key, String property) {
        return getCell(getDataRowIndex(key, property), getDataColumnIndex(key, property));
    }

    public void setDataValue(String key, String property, String value) {
        Cell cell = getCell(key, property);
        cell.setCellStyle(getCell(getDataRowIndex(key, property), 0).getCellStyle());
        cell.setCellValue(value);
    }

    public void setDataValue(String key, String property, JsonElement value) {
        if (value.isJsonArray()) {
            setDataValue(key, property, value.getAsJsonArray().toString());
        } else {
            setDataValue(key, property, value.getAsString());
        }
    }

    private void saveDataSource(String filePath) {
        if (workbook != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                workbook.write(fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                throw new GenericRuntimeException(e);
            }
        }
    }

    public void saveDataSource() {
        saveDataSource(filePath);
    }

    public void saveDataSourceCopy() {
        saveDataSource(fileCopyPath);
    }

    private String getCellFillColorCode(Cell cell) {
        String colorCode;

        try {
            colorCode = ((ExtendedColor) cell.getCellStyle().getFillForegroundColorColor()).getARGBHex();
        } catch (Exception e) {
            colorCode = null;
        }

        return colorCode;
    }

    private boolean cellFillColorCodeIsWhite(Cell cell) {
        String cellFillColorCode = getCellFillColorCode(cell);
        return ((cellFillColorCode == null) || cellFillColorCode.equalsIgnoreCase("FFFFFFFF"));
    }

    private void buildColumnNameListObjects(Row headerRow, Iterator<Row> iterator) {
        for (Cell headerCell : headerRow) {

            String headerCellValue = headerCell.getStringCellValue().trim();

            checkIfHeaderCellValueIsDuplicated(headerCellValue);

            if (!dataFormatter.formatCellValue(headerCell).trim().equals("")) {
                if (cellFillColorCodeIsWhite(headerCell)) {
                    if (headerCell.getColumnIndex() > 0)
                        filteredPropertyList.add(headerCellValue);
                    filteredColumnNameList.add(headerCellValue);
                }
                if (headerCell.getColumnIndex() > 0) fullPropertyList.add(headerCellValue);
                fullColumnNameList.add(headerCellValue);
            }
        }

        if (iterator.hasNext()) iterator.next();
    }

    private void checkIfHeaderCellValueIsDuplicated(String headerCellValue) {
        if (fullColumnNameList.contains(headerCellValue)) {
            throw new GenericRuntimeException("The header cell value <" + headerCellValue + "> is duplicated in the sheet <" + sheetName + "> of the data source file <" + filePath + ">");
        }
    }

    private void buildDataValueSubObjects(Cell propertyCell, String property, Cell valueCell, String value, int rowIndex, int columnIndex) {
        if (!dataFormatter.formatCellValue(propertyCell).trim().equals("")) {
            if (cellFillColorCodeIsWhite(propertyCell) && cellFillColorCodeIsWhite(valueCell)) {
                filteredDataValueJsonObject.addProperty(property, value);
            }
            fullDataValueJsonObject.addProperty(property, value);

            cellAddressJsonArray.add(rowIndex);
            cellAddressJsonArray.add(columnIndex);
            dataAddressJsonObject.add(property, cellAddressJsonArray);
        }
    }

    private void buildDataValueObjects(Cell rowKeyCell, String rowKeyValue, JsonObject filteredDataValueJsonObject, JsonObject fullDataValueJsonObject, JsonObject dataAddressJsonObject) {
        if ((rowKeyValue != null) && (!rowKeyValue.equals(""))) {
            if (fullDataValues.has(rowKeyValue)) {
                throw new GenericRuntimeException("The row key <" + rowKeyValue + "> is duplicated in the sheet <" + sheetName + "> of the data source file <" + filePath + ">");
            }

            if (cellFillColorCodeIsWhite(rowKeyCell)) {
                filteredDataValues.add(rowKeyValue, filteredDataValueJsonObject);
            }

            fullDataValues.add(rowKeyValue, fullDataValueJsonObject);
            dataAddresses.add(rowKeyValue, dataAddressJsonObject);
        }
    }

    private void loadData() {
        workbook = new XlsxWorkbook(filePath).getWorkbook();

        Sheet sheet = workbook.getSheet(sheetName);
        Iterator<Row> iterator = sheet.iterator();

        Row headerRow = sheet.getRow(0);
        int lastCellNum = headerRow.getLastCellNum();

        buildColumnNameListObjects(headerRow, iterator);

        while (iterator.hasNext()) {
            Row row = iterator.next();

            int rowIndex = row.getRowNum();

            filteredDataValueJsonObject = new JsonObject();
            fullDataValueJsonObject = new JsonObject();
            dataAddressJsonObject = new JsonObject();

            Cell rowKeyCell = null;
            String rowKeyValue = null;

            Cell propertyCell;
            String property = null;

            Cell valueCell;
            String value;

            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = row.getCell(i, CREATE_NULL_AS_BLANK);
                cellAddressJsonArray = new JsonArray();

                int columnIndex = cell.getColumnIndex();

                if (columnIndex == 0) {
                    rowKeyCell = cell;
                    rowKeyValue = dataFormatter.formatCellValue(rowKeyCell).trim();
                } else {
                    propertyCell = headerRow.getCell(columnIndex);
                    property = dataFormatter.formatCellValue(propertyCell).trim();

                    valueCell = cell;
                    value = dataFormatter.formatCellValue(valueCell).trim();

                    buildDataValueSubObjects(propertyCell, property, valueCell, value, rowIndex, columnIndex);
                }
            }

            buildDataValueObjects(rowKeyCell, rowKeyValue, filteredDataValueJsonObject, fullDataValueJsonObject, dataAddressJsonObject);
        }
    }

    private void tryFindDataSourceFileAndReturnPath(String filePath) {
        String foundFilePath = new FileOprs().tryFindFileAndReturnPath(filePath);

        if (new StringOprs().isEmptyOrNull(foundFilePath)) {
            throw new GenericRuntimeException("The data source file <" + filePath + "> was not found");
        } else {
            this.filePath = foundFilePath;
        }
    }
}