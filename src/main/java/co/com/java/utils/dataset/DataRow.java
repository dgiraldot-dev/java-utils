package co.com.java.utils.dataset;

import com.google.gson.JsonObject;
import co.com.java.utils.json.JsonOprs;

public class DataRow {
  
  private JsonOprs jsonOprs = new JsonOprs();  
  private JsonObject dataRow;

  public DataRow(JsonObject dataRow) {
    this.dataRow = dataRow;
  }
  
  public String getDataValue(String columnName) {    
    return dataRow.get(columnName).getAsString();
  }
  
  public void printData() {
    jsonOprs.printJsonObject(dataRow);
  }
  
  public JsonObject getData() {
    return dataRow;
  }
}
