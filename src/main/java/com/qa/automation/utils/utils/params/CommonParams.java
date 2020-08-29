package com.qa.automation.utils.utils.params;

import static com.qa.automation.utils.utils.params.ProjectConfigProperties.*;
import com.qa.automation.utils.java.utils.os.CommonOsOprs;

public class CommonParams {

  //PARAMS FROM PROJECT CONFIG PROPERTIES FILE (project.config.properties)

  public static final String PATH_SEPARATOR_CHAR = new CommonOsOprs().getPathSeparatorChar();
  public static final String CONTENT_ENCODING_TYPE = getAsString("content.encoding.type");
  public static final Boolean SHOW_ERROR_MESSAGE_DIALOG_BY_THROW_EXCEPTION = getAsBoolean("show.error.message.dialog.by.throw.exception");
  public static final Boolean SHOW_WARN_MESSAGE_DIALOG_BY_CATCH_EXCEPTION = getAsBoolean("show.warn.message.dialog.by.catch.exception");
  public static final Boolean END_SYSTEM_RUN_BY_THROW_EXCEPTION = getAsBoolean("end.system.run.by.throw.exception");

  //OTHER PARAMS

  public static final String JSON_EXTENSION = ".json";
}