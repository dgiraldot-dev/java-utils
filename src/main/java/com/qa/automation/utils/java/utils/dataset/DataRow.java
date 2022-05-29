package com.qa.automation.utils.java.utils.dataset;

import com.google.gson.JsonObject;
import com.qa.automation.utils.java.utils.json.JsonOprs;

public class DataRow {
  
  private JsonOprs jsonOprs = new JsonOprs();  
  private JsonObject dataRowJsonObject;

  public DataRow(JsonObject dataRow) {
    this.dataRowJsonObject = dataRow;
  }
  
  public String getDataValue(String columnName) {    
    return dataRowJsonObject.get(columnName).getAsString();
  }

  public JsonObject getData() {
    return dataRowJsonObject;
  }
}
