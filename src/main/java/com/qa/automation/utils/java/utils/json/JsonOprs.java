package com.qa.automation.utils.java.utils.json;

import com.google.gson.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.GenericRuntimeException;
import com.qa.automation.utils.java.utils.params.JavaUtilsParams;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonOprs {

    private static final Logger LOGGER = LogManager.getLogger(JsonOprs.class);

    private FileInputStream fileInputStream;
    private Workbook workbook;
    private DataFormatter formatter = new DataFormatter();

    public JsonOprs() {
        // Initialize without attributes
    }

    public void saveJsonFile(JsonObject jsonObject, String jsonFilePath) {

        FileOprs fileOprs = new FileOprs();

        fileOprs.createDirectory(fileOprs.getDirectoryPathFromFilePath(jsonFilePath));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(jsonFilePath), Charset.forName(JavaUtilsParams.CONTENT_ENCODING_TYPE))) {
            writer.write(gson.toJson(jsonObject));
        } catch (Exception e) {
            // Nothing
        }
    }

    public void saveJsonFile(String jsonString, String jsonFilePath) {
        saveJsonFile(stringToJsonObject(jsonString), jsonFilePath);
    }

    public String formatJsonString(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return formatJsonObject(jsonObject);
    }

    public String formatJsonObject(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public String formatJsonArrayString(String jsonArrayString) {
        JsonArray jsonArray = JsonParser.parseString(jsonArrayString).getAsJsonArray();
        return formatJsonArrayObject(jsonArray);
    }

    public String formatJsonArrayObject(JsonArray jsonArrayObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonArrayObject);
    }

    public JsonObject stringToJsonObject(String jsonString) {
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public String jsonObjectToString(JsonObject jsonObject) {
        return formatJsonObject(jsonObject);
    }

    public JsonArray stringToJsonArray(String jsonArrayString) {
        JsonElement jsonElement = JsonParser.parseString(jsonArrayString);
        return jsonElement.getAsJsonArray();
    }

    public JsonArray stringArrayListToJsonArray(List<String> arrayList) {
        JsonArray jsonArray = new JsonArray();

        for (String arrayListElement : arrayList) {
            jsonArray.add(arrayListElement);
        }

        return jsonArray;
    }

    public JsonObject mapToJsonObject(Map<String, Object> map) {
        return stringToJsonObject(mapToJsonString(map));
    }

    public String mapToJsonString(Map<String, Object> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public boolean existStringInStringsJsonArray(String string, JsonArray jsonArray) {
        boolean response = false;

        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.getAsString().equalsIgnoreCase(string)) {
                response = true;
                break;
            }
        }

        return response;
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(String excelDataFilePath) {
        return excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(excelDataFilePath, false);
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(String excelDataFilePath, boolean trimCellValues) {
        JsonObject jsonObject = new JsonObject();

        try {
            fileInputStream = new FileInputStream(new File(excelDataFilePath));
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            // Nothing
        }

        int numberOfSheets = workbook.getNumberOfSheets();

        String sheetName;
        Sheet sheet;

        for (int i = 0; i < numberOfSheets; i++) {
            sheetName = workbook.getSheetName(i);
            sheet = workbook.getSheetAt(i);
            jsonObject.add(sheetName, excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(sheet, trimCellValues));
        }

        try {
            fileInputStream.close();
            workbook.close();
        } catch (Exception e) {
            // Nothing
        }

        return jsonObject;
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(String excelDataFilePath, String excelSheetName) {
        return excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(excelDataFilePath, excelSheetName, false);
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(String excelDataFilePath, String excelSheetName, boolean trimCellValues) {
        JsonObject jsonObject;

        try {
            fileInputStream = new FileInputStream(new File(excelDataFilePath));
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            // Nothing
        }

        Sheet sheet = workbook.getSheet(excelSheetName);

        jsonObject = excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(sheet, trimCellValues);

        try {
            fileInputStream.close();
            workbook.close();
        } catch (Exception e) {
            // Nothing
        }

        return jsonObject;
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(Sheet sheet) {
        return excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(sheet, false);
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(Sheet sheet, boolean trimCellValues) {
        JsonObject jsonObject = new JsonObject();
        JsonObject columnNames = new JsonObject();
        JsonArray columnValues = null;
        int maxColumnIndex = -1;

        StringOprs stringOprs = new StringOprs();

        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext()) {
            Row row = iterator.next();

            String columnName;
            String cellValue;

            if (formatter.formatCellValue(row.getCell(0)) != null) {
                if (row.getRowNum() == 0) { // Columnas
                    for (Cell cell : row) {
                        cellValue = formatter.formatCellValue(cell).trim();
                        columnNames.addProperty(Integer.toString(cell.getColumnIndex()), cellValue);
                        if (!cellValue.equals("")) {
                            columnNames.addProperty(Integer.toString(cell.getColumnIndex()), cellValue);
                            columnValues = new JsonArray();

                            if (jsonObject.get(cellValue) == null) {
                                jsonObject.add(cellValue, columnValues);
                            } else {
                                throw new GenericRuntimeException("The column name <" + cellValue + "> located in column number <" + (cell.getColumnIndex() + 1) + "> is duplicated");
                            }

                            maxColumnIndex = maxColumnIndex + 1;
                        } else {
                            break;
                        }
                    }
                } else if (row.getRowNum() >= 1) { // Registros
                    for (int i = 0; i <= maxColumnIndex; i++) {
                        Cell cell = row.getCell(i);
                        if (i <= maxColumnIndex) {

                            columnName = columnNames.get(Integer.toString(i)).getAsString();
                            cellValue = formatter.formatCellValue(cell);

                            if (trimCellValues) {
                                cellValue = cellValue.trim();
                            }

                            if (!stringOprs.isEmptyOrNull(cellValue) && !existStringInStringsJsonArray(cellValue, jsonObject.get(columnName).getAsJsonArray())) {
                                jsonObject.get(columnName).getAsJsonArray().add(cellValue);
                            } else if (!stringOprs.isEmptyOrNull(cellValue)) {
                                LOGGER.warn("El dato <" + cellValue + "> ubicado en la fila número <" + (cell.getRowIndex() + 1) + "> se encuentra repetido en la columna <" + columnName + ">, se tendrá en cuenta una sola ocurrencia.");
                            }
                        } else {
                            break;
                        }
                    }
                }
            } else {
                break;
            }
        }

        return jsonObject;
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueFirstColumnValueKeyByRow(String excelDataFilePath, String excelSheetName) {
        return excelFileSheetToJsonObjecttWithUniqueFirstColumnValueKeyByRow(excelDataFilePath, excelSheetName, false);
    }

    public JsonObject excelFileSheetToJsonObjecttWithUniqueFirstColumnValueKeyByRow(String excelDataFilePath, String excelSheetName, boolean trimCellValues) {
        JsonObject jsonObject = new JsonObject();
        JsonObject columnNames = new JsonObject();
        JsonObject recordJsonObject = null;
        int maxColumnIndex = -1;

        try {
            fileInputStream = new FileInputStream(new File(excelDataFilePath));
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            // Nothing
        }

        Sheet sheet = workbook.getSheet(excelSheetName);
        Iterator<Row> iterator = sheet.iterator();

        while (iterator.hasNext()) {
            Row row = iterator.next();
            recordJsonObject = new JsonObject();
            String key = null;

            String columnName;
            String cellValue;

            if (!formatter.formatCellValue(row.getCell(0)).equals("")) {
                if (row.getRowNum() == 0) { // Columnas
                    for (Cell cell : row) {
                        cellValue = formatter.formatCellValue(cell).trim();
                        columnNames.addProperty(Integer.toString(cell.getColumnIndex()), cellValue);
                        if (!cellValue.equals("")) {
                            columnNames.addProperty(Integer.toString(cell.getColumnIndex()), cellValue);
                            maxColumnIndex = maxColumnIndex + 1;
                        } else {
                            break;
                        }
                    }
                } else if (row.getRowNum() >= 1) { // Registros
                    for (int i = 0; i <= maxColumnIndex; i++) {
                        Cell cell = row.getCell(i);
                        if (i <= maxColumnIndex) {

                            columnName = columnNames.get(Integer.toString(i)).getAsString();
                            cellValue = formatter.formatCellValue(cell);

                            if (i == 0) { // Key
                                key = cellValue;
                            } else {
                                if (trimCellValues) {
                                    cellValue = cellValue.trim();
                                }
                                recordJsonObject.addProperty(columnName, cellValue);
                            }
                        } else {
                            break;
                        }
                    }

                    jsonObject.add(key, recordJsonObject);
                }
            } else {
                break;
            }
        }

        try {
            fileInputStream.close();
            workbook.close();
        } catch (Exception e) {
            // Nothing
        }

        return jsonObject;
    }

    public DocumentContext getDocumentContext(String jsonFilePath) {

        File jsonObjectFile = new File(jsonFilePath);

        DocumentContext documentContext = null;

        try {
            documentContext = JsonPath.using(Configuration.defaultConfiguration()).parse(jsonObjectFile);
        } catch (IOException e) {
            // Nothing
        }

        return documentContext;
    }

    public JsonObject getJsonObject(String jsonFileName) {
        return JsonParser.parseString(new FileOprs().getFileContent(jsonFileName)).getAsJsonObject();
    }

    public static JSONArray parseJsonArrayStringToJSONArrayObject(String jsonArrayString) {
        try {
            return (JSONArray) new JSONParser().parse(jsonArrayString);
        } catch (ParseException e) {
            return new JSONArray();
        }
    }
}
