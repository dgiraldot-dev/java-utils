package com.qa.automation.utils.java.utils.dataset;

import com.google.gson.JsonObject;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.JavaOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.json.JsonOprs;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Dataset {

    private static final Logger LOGGER = LogManager.getLogger(Dataset.class);

    private String excelDataFilePath;
    private FileOprs fileOprs = new FileOprs();
    private StringOprs stringOprs = new StringOprs();
    private JavaOprs javaOprs = new JavaOprs();
    private JsonOprs jsonOprs = new JsonOprs();
    private JsonObject datasetJsonObject;

    public Dataset(String excelDataFilePath) {
        setDataset(excelDataFilePath, "Dataset", false);
    }

    public Dataset(String excelDataFilePath, boolean trimCellValues) {
        setDataset(excelDataFilePath, "Dataset", trimCellValues);
    }

    public Dataset(String excelDataFilePath, String excelSheetName) {
        setDataset(excelDataFilePath, excelSheetName, false);
    }

    public Dataset(String excelDataFilePath, String excelSheetName, boolean trimCellValues) {
        setDataset(excelDataFilePath, excelSheetName, trimCellValues);
    }

    private void setDataset(String excelDataFilePath, String excelSheetName, Boolean trimCellValues) {
        tryFindExcelFile(excelDataFilePath);
        datasetJsonObject = jsonOprs.excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(this.excelDataFilePath, excelSheetName, trimCellValues);
    }

    private void tryFindExcelFile(String excelDataFilePath) {
        if (stringOprs.isEmptyOrNull(fileOprs.checkIfExistsFileAndGetAbsolutePath(excelDataFilePath))) {
            this.excelDataFilePath = fileOprs.findFileAndGetAbsoluteFilePath(javaOprs.getThisProjectDirectoryPath(), excelDataFilePath);
        } else {
            this.excelDataFilePath = excelDataFilePath;
        }

        if (stringOprs.isEmptyOrNull(this.excelDataFilePath)) {
            LOGGER.log(Level.INFO, new StringBuilder("El archivo excel <").append(excelDataFilePath).append("> con los datos de prueba no existe").toString());
        }
    }

    public JsonObject getDataRow(String id) {
        return datasetJsonObject.getAsJsonObject(id);
    }

    public String getDataValue(String path) {
        String[] dataRecordArray = path.split("\\.");
        return datasetJsonObject.getAsJsonObject(dataRecordArray[0]).get(dataRecordArray[1]).getAsString();
    }

    public String getDataValue(String id, String columnName) {
        return datasetJsonObject.getAsJsonObject(id).get(columnName).getAsString();
    }

    public JsonObject getData() {
        return datasetJsonObject;
    }

    public String getExcelDataFilePath() {
        return excelDataFilePath;
    }
}
