package com.qa.automation.utils.java.utils.params;

import java.util.HashMap;
import com.qa.automation.utils.java.utils.exception.JavaException;

public class RuntimeParams {

  private static final HashMap<String, Object> DYNAMIC_PARAMS = new HashMap<String, Object>();
  
  static {

  }

  public static synchronized Object get(String paramName){
    return DYNAMIC_PARAMS.get(paramName);
  }

  public static synchronized void add(String paramName, Object paramValue){
    Object currentParamValue = DYNAMIC_PARAMS.get(paramName);
    if (currentParamValue == null) {
      DYNAMIC_PARAMS.put(paramName, paramValue);
    } else {
      new JavaException().catchException("Custom Parameter <" + paramName + "> already exists with value <" + currentParamValue + ">");
    }
  }
  
  public static synchronized void set(String paramName, Object paramValue){
    if (DYNAMIC_PARAMS.get(paramName) != null) {
      DYNAMIC_PARAMS.put(paramName, paramValue); 
    } else {
      printParamsList();
      new JavaException().catchException("Custom Parameter <" + paramName + "> does not exist");
    }
  }
  
  public static HashMap<String, Object> getParamsHashMapObject() {
    return DYNAMIC_PARAMS;
  }
  
  public static void printParamsList() {
    System.out.println("***** CURRENT CUSTOM PARAMS LIST ----------------------------------------------------------------------------------------------------------------------------\r\n");
    DYNAMIC_PARAMS.forEach((key, value) -> System.out.println(key + " = " + value));
    System.out.println("\r\n***** -------------------------------------------------------------------------------------------------------------------------------------------------------");
  }
}