package com.qa.automation.utils.java.utils.params;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class RuntimeParams {

    private static final Logger LOGGER = LogManager.getLogger(RuntimeParams.class);
    private static final HashMap<String, Object> DYNAMIC_PARAMS = new HashMap<>();

    private RuntimeParams() {
        // Initialize without attributes
    }

    public static synchronized Object get(String paramName) {
        return DYNAMIC_PARAMS.get(paramName);
    }

    public static synchronized void add(String paramName, Object paramValue) {
        Object currentParamValue = DYNAMIC_PARAMS.get(paramName);
        if (currentParamValue == null) {
            DYNAMIC_PARAMS.put(paramName, paramValue);
        } else {
            LOGGER.log(Level.WARN, "Custom Parameter <" + paramName + "> already exists with value <" + currentParamValue + ">");
        }
    }

    public static synchronized void set(String paramName, Object paramValue) {
        if (DYNAMIC_PARAMS.get(paramName) != null) {
            DYNAMIC_PARAMS.put(paramName, paramValue);
        } else {
            printParamsList();
            LOGGER.log(Level.ERROR, "Custom Parameter <" + paramName + "> does not exist");
            System.exit(1);
        }
    }

    public static HashMap<String, Object> getParamsHashMapObject() {
        return DYNAMIC_PARAMS;
    }

    public static void printParamsList() {
        LOGGER.log(Level.INFO, "***** CURRENT CUSTOM PARAMS LIST ----------------------------------------------------------------------------------------------------------------------------\r\n");
        DYNAMIC_PARAMS.forEach((key, value) -> LOGGER.log(Level.INFO, key + " = " + value));
        LOGGER.log(Level.INFO, "\r\n***** -------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}