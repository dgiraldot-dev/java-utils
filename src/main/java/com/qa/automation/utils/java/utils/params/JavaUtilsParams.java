package com.qa.automation.utils.java.utils.params;

import com.qa.automation.utils.java.utils.os.CommonOsOprs;

import static com.qa.automation.utils.java.utils.params.ProjectConfigProperties.getAsString;

public class JavaUtilsParams {

    //PARAMS FROM PROJECT CONFIG PROPERTIES FILE (project.config.properties)

    public static final String PATH_SEPARATOR_CHAR = new CommonOsOprs().getPathSeparatorChar();
    public static final String CONTENT_ENCODING_TYPE = getAsString("content.encoding.type");
    public static final String JSON_EXTENSION = ".json";

    //OTHER PARAMS
}