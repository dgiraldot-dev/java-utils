package com.qa.automation.utils.java.utils.common;

public class NumericOprs {
  
  private NumericOprs() {
    // initialize without attributes
  }
  
  public static Double getMinimumMarginValue(Double value) {
    
    int decimalPlaces = String.valueOf(value).split("\\.")[1].length();
    
    String outputValue = "";
    
    for (int i = 0; i <= decimalPlaces; i++) {
      if (decimalPlaces == 1) {
        outputValue = "0.1";
        break;
      } else if (i == 0) {
        outputValue = "0.";
      } else if (i == decimalPlaces) {
        outputValue = (new StringBuilder()).append(outputValue).append("1").toString();
      } else {
        outputValue = (new StringBuilder()).append(outputValue).append("0").toString();
      }
    }
    
    return Double.valueOf(outputValue);
  }
}
