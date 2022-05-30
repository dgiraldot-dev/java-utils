package com.qa.automation.utils.java.utils.params;

import com.qa.automation.utils.java.utils.exception.GenericRuntimeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
            LOGGER.warn("Custom Parameter <" + paramName + "> already exists with value <" + currentParamValue + ">");
        }
    }

    public static synchronized void set(String paramName, Object paramValue) {
        if (DYNAMIC_PARAMS.get(paramName) != null) {
            DYNAMIC_PARAMS.put(paramName, paramValue);
        } else {
            printParamsList();
            throw new GenericRuntimeException("Custom Parameter <" + paramName + "> does not exist");
        }
    }

    public static Map<String, Object> getParamsHashMapObject() {
        return DYNAMIC_PARAMS;
    }

    public static void printParamsList() {
        LOGGER.info("***** CURRENT CUSTOM PARAMS LIST ----------------------------------------------------------------------------------------------------------------------------\r\n");
        DYNAMIC_PARAMS.forEach((key, value) -> LOGGER.info(key + " = " + value));
        LOGGER.info("\r\n***** -------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}