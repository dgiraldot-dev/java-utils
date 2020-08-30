package com.qa.automation.utils.java.utils.dataset;

import com.google.gson.JsonObject;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.JavaOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.JavaException;
import com.qa.automation.utils.java.utils.json.JsonOprs;

public class Dataset {
  
  private String excelDataFilePath;
  private FileOprs fileOprs = new FileOprs();
  private StringOprs stringOprs = new StringOprs();
  private JavaOprs javaOprs = new JavaOprs();
  private JsonOprs jsonOprs = new JsonOprs();  
  private JsonObject dataset;
  
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
    dataset = jsonOprs.excelFileSheetToJsonObjecttWithUniqueColumnNameKeyByColumn(this.excelDataFilePath, excelSheetName, trimCellValues);
  }
  
  private void tryFindExcelFile(String excelDataFilePath) {
    if (stringOprs.isEmptyOrNull(fileOprs.checkIfExistsFileAndGetAbsolutePath(excelDataFilePath))) {
      this.excelDataFilePath = fileOprs.findFileAndGetAbsoluteFilePath(javaOprs.getThisProjectDirectoryPath(), excelDataFilePath);
    } else {
      this.excelDataFilePath = excelDataFilePath;
    }
    
    if (stringOprs.isEmptyOrNull(this.excelDataFilePath)) {
      new JavaException().throwException("El archivo excel <" + excelDataFilePath + "> con los datos de prueba no existe");
    }
  }
  
  public JsonObject getDataRow(String id) {
    return dataset.getAsJsonObject(id);
  }
  
  public String getDataValue(String path) {    
    String[] dataRecordArray = path.split("\\.");    
    return dataset.getAsJsonObject(dataRecordArray[0]).get(dataRecordArray[1]).getAsString();
  }
  
  public String getDataValue(String id, String columnName) {    
    return dataset.getAsJsonObject(id).get(columnName).getAsString();
  }
  
  public void printData() {
    jsonOprs.printJsonObject(dataset);
  }
  
  public JsonObject getData() {
    return dataset;
  }
  
  public String getExcelDataFilePath() {
    return excelDataFilePath;
  }
}
