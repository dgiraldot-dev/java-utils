package com.qa.automation.utils.java.utils.params;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.JavaException;

public class ProjectConfigProperties {
  
  private static final String PROJECT_CONFIG_PROPERTIES_FILE_NAME = "project.config.properties";
  private static final String MAIN_RESOURCES_DIRECTORY_PATH = "src/main/resources/";
  private static final String TEST_RESOURCES_DIRECTORY_PATH = "src/test/resources/";
  
  private static Properties properties = null;
  
  private static StringOprs stringOprs = new StringOprs();

  static {
    File file = new File(MAIN_RESOURCES_DIRECTORY_PATH + PROJECT_CONFIG_PROPERTIES_FILE_NAME);
    if (!file.exists()) file = new File(TEST_RESOURCES_DIRECTORY_PATH + PROJECT_CONFIG_PROPERTIES_FILE_NAME);
    addConfigPropertiesFile(file);
  }
  
  public static void addConfigPropertiesFile(String configPropertiesFilePath) {
    try {
      File configPropertiesFileObject = new File(configPropertiesFilePath);
      addConfigPropertiesFile(configPropertiesFileObject);
    } 
    catch (Exception e) {
      new JavaException().throwException("No fue posible cargar el archivo de propiedades de configuración del proyecto", e, true);
    }
  }

  public static void addConfigPropertiesFile(File configPropertiesFileObject) {
    if (ProjectConfigProperties.properties == null) ProjectConfigProperties.properties = new Properties();
    try {
      InputStream inputStream = new FileInputStream(configPropertiesFileObject);
      Properties properties = new Properties();
      properties.load(inputStream);
      inputStream.close();
      ProjectConfigProperties.properties.putAll(properties);
    } 
    catch (Exception e) {
      new JavaException().throwException("No fue posible cargar el archivo de propiedades de configuración del proyecto", e, true);
    }
  }
  
  public static void setProperty(String key, String value) {
    if (!stringOprs.isEmptyOrNull(key)) key = key.trim();
    if (!stringOprs.isEmptyOrNull(value)) value = value.trim();
    if (properties != null) properties.setProperty(key, value);
  }

  public static Properties getProperties() {
    return properties;
  }

  public static String getAsString(String key) {
    if (!stringOprs.isEmptyOrNull(key)) key = key.trim();
    String value = properties == null ? null : properties.getProperty(key, "");
    if (stringOprs.isEmptyOrNull(value)) {
      new JavaException().throwException("El parámetro de configuración del proyecto <" + key + "> no existe");
    }
    return value == null ? null : value.trim();
  }

  public static Boolean getAsBoolean(String key) {
    String value = getAsString(key);
    return Boolean.parseBoolean(value); 
  }

  public static int getAsInteger(String key) {
    String value = getAsString(key);
    return Integer.parseInt(value); 
  }
  
  public static double getAsDouble(String key) {
    String value = getAsString(key);
    return Double.parseDouble(value); 
  }
  
  public static long getAsLong(String key) {
    String value = getAsString(key);
    return Long.parseLong(value); 
  }
  
  public static float getAsFloat(String key) {
    String value = getAsString(key);
    return Float.parseFloat(value); 
  }
  
  public static byte getAsByte(String key) {
    String value = getAsString(key);
    return Byte.parseByte(value); 
  }
  
  public static short getAsShort(String key) {
    String value = getAsString(key);
    return Short.parseShort(value); 
  }
}