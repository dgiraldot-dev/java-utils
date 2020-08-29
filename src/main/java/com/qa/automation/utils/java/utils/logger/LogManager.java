package com.qa.automation.utils.java.utils.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import com.qa.automation.utils.java.utils.JavaOprs;
import com.qa.automation.utils.java.utils.common.DateTimeOprs;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;

public class LogManager implements Runnable {

  private static final String REGEX_JAVA_UTILS_PACKAGE_NAME_TO_SKIP = "co\\.com\\.java\\.utils.*";
  private static final String DEFAULT_LOG_DIRECTORY_NAME = "logs";
  private static final String EXECUTION_TIME_STAMP_FORMAT = "yyyyMMdd_HHmmss";
  private static String defaultLogFileName;
  private static final String DEFAULT_LOG_FILE_EXTENSION = ".log";
  private static final String ASYNC = "ASYNC";
  private static final String FILE_ROLLING = "FILE-ROLLING-";
  private static final String LOGBACK_LOG_DIRECTORY_PATH = "logback.log.directory.path";
  private static final String LOGBACK_LOG_FILE_NAME = "logback.log.file.name";
  private static final String LOGBACK_LOG_FILE_EXTENSION = "logback.log.file.extension";

  private String regexOfPackageNamesToSkip = this.getClass().getName() + "|" + "java\\.lang\\.Thread";

  public String logDirectoryPath;
  public String logFileName;
  public String logFileExtension;
  public String logFilePath;

  public String currentClassName;
  public String currentMethodName;

  private boolean loggerFactoryFlag = true;
  public static Logger logger;

  private FileOprs fileOprs = new FileOprs();
  private StringOprs stringOprs = new StringOprs();
  private JavaOprs javaOprs = new JavaOprs();

  public LogManager() {
    setDefaultLogFileName();
    setupLogger(fileOprs.normalizePath(javaOprs.getThisProjectDirectoryPath(), DEFAULT_LOG_DIRECTORY_NAME), defaultLogFileName, DEFAULT_LOG_FILE_EXTENSION);
  }

  public LogManager(String logDirectoryPath) {  
    setDefaultLogFileName();
    setupLogger(logDirectoryPath, defaultLogFileName, DEFAULT_LOG_FILE_EXTENSION);
  }

  public LogManager(String logDirectoryPath, String logFileName) {   
    setupLogger(logDirectoryPath, logDirectoryPath, DEFAULT_LOG_FILE_EXTENSION);
  }

  public LogManager(String logDirectoryPath, String logFileName, String logFileExtension) {  
    setupLogger(logDirectoryPath, logDirectoryPath, logDirectoryPath);
  }

  private void setupLogger(String logDirectoryPath, String logFileName, String logFileExtension) {
    if ((getLoggerContext() == null) || (!stringOprs.evaluateRegex(REGEX_JAVA_UTILS_PACKAGE_NAME_TO_SKIP, getCallerClassFullName(), false))) {
      this.logDirectoryPath = logDirectoryPath;
      this.logFileName = logFileName;
      this.logFileExtension = logFileExtension;
      setSystemProperties();
      setLogFilePathInRuntime(setLogFilePath());
      loadThisClass();
    } else {
      regexOfPackageNamesToSkip = regexOfPackageNamesToSkip + "|" + REGEX_JAVA_UTILS_PACKAGE_NAME_TO_SKIP;
      loggerFactoryFlag = false;
      getSystemProperties();
    }
  }

  public LoggerContext getLoggerContext() {
    LoggerContext loggerContext;
    try {
      loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();
    } catch (Exception e) {
      loggerContext = null;
    }

    return loggerContext;
  }

  private void setLogFilePathInRuntime(String logFilePath) { 
    LoggerContext loggerContext = getLoggerContext();
    if (loggerContext != null) {
      ch.qos.logback.classic.Logger logger = loggerContext.getLogger(getCallerClassPackageFirstName());
      AsyncAppender asyncAppender = (AsyncAppender) logger.getAppender(ASYNC);
      try {
        RollingFileAppender<?> rollingFileAppender = (RollingFileAppender<?>) asyncAppender.getAppender(FILE_ROLLING + logFileName);
        rollingFileAppender.setFile(logFilePath);
        rollingFileAppender.openFile(logFilePath);
      } catch (Exception e) {

      }
    }
  }

  private String setLogFilePath() {
    logFilePath = fileOprs.normalizePath(logDirectoryPath, logFileName + logFileExtension);
    return logFilePath;
  }
  
  private void setDefaultLogFileName() {
    if (stringOprs.isEmptyOrNull(defaultLogFileName))
      defaultLogFileName = new DateTimeOprs().getCurrentTime(EXECUTION_TIME_STAMP_FORMAT);
  }

  private void setSystemProperties() {
    System.setProperty("logback.loggger.name", getCallerClassPackageFirstName());
    if (logDirectoryPath != null) System.setProperty(LOGBACK_LOG_DIRECTORY_PATH, logDirectoryPath);
    if (logFileName != null) System.setProperty(LOGBACK_LOG_FILE_NAME, logFileName);
    if (logFileExtension != null) System.setProperty(LOGBACK_LOG_FILE_EXTENSION, logFileExtension);
  }

  private void getSystemProperties() {
    logDirectoryPath = System.getProperty(LOGBACK_LOG_DIRECTORY_PATH);
    logFileName = System.getProperty(LOGBACK_LOG_FILE_NAME);
    logFileExtension = System.getProperty(LOGBACK_LOG_FILE_EXTENSION);
    setLogFilePath();
  }

  @Override
  public void run() {
    loggerFactoryFlag = false;
    MDC.put(LOGBACK_LOG_DIRECTORY_PATH, logDirectoryPath);
    MDC.put(LOGBACK_LOG_FILE_NAME, logFileName);
    info("Logger started: " + logFilePath);      
  }

  public void reset() {
    MDC.remove(LOGBACK_LOG_FILE_NAME);
  }

  private void startLogger(String className) {
    try {
      logger = LoggerFactory.getLogger(Class.forName(className));
      if (loggerFactoryFlag) {
        run();
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public String getLogDirectoryPath() {
    return logDirectoryPath;
  }

  public String getLogFilePath() {
    return logFilePath;
  }

  public Logger getLoggerInstance() {
    return logger;
  }

  public String getCurrentClassMethodName() {
    return currentMethodName;
  }

  public void setCurrentClassName(String className) {
    currentClassName = className;
  }

  public String getCurrentClassName() {
    return currentClassName;
  }

  public void loadThisClass() {
    currentClassName = getCallerClassFullName();
    currentMethodName = getCallerClassMethodName();
    startLogger(currentClassName);
  }

  public void loadClass(String className, String methodName) {
    currentClassName = className;
    currentMethodName = methodName;
    startLogger(className);
  }

  public void loadClass(String className) {
    loadClass(className, null);
  }

  public String getCallerClassFullName() {
    return getStackTraceElement().getClassName();
  }


  public String getCallerClassSimpleName() {
    String className = getCallerClassFullName();
    return className.substring(className.lastIndexOf(".") + 1, className.length()); 
  }

  public String getCallerClassMethodName() {
    return getStackTraceElement().getMethodName();
  }

  public String getCallerClassPackageFullName() {
    String callerFullClassName = getCallerClassFullName();
    return callerFullClassName.substring(0, callerFullClassName.lastIndexOf("."));
  }

  public String getCallerClassPackageFirstName() {
    String callerClassPackageFullName = getCallerClassPackageFullName();
    return callerClassPackageFullName.substring(0, callerClassPackageFullName.indexOf("."));
  }

  public void printStackTrace() {
    System.out.println(getStackTraceString());
  }

  public String getStackTraceString() {     
    String stackTraceString = "";

    StackTraceElement[] stackTraceElements;

    stackTraceElements = Thread.currentThread().getStackTrace();    

    int stackTraceElementsLength = stackTraceElements.length;

    for (int i = 0; i < stackTraceElementsLength; i++) {
      if ((stackTraceElementsLength - 1) > i) {
        stackTraceString = stackTraceString + stackTraceElements[i] + "\r\n";
      } else {
        stackTraceString = stackTraceString + stackTraceElements[i];
      }
    }

    return stackTraceString;
  }

  public StackTraceElement getStackTraceElement() {
    StackTraceElement stackTraceElement = null;
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

    for (StackTraceElement element : stackTraceElements) {
      String elementClassName = element.getClassName();
      if (!stringOprs.evaluateRegex(regexOfPackageNamesToSkip, elementClassName, false)) {
        stackTraceElement = element;
        break;
      }
    }

    return stackTraceElement;
  }

  private String buildLogMessage(String message) {
    String methodName = getStackTraceElement().getMethodName();
    String lineNumber = String.valueOf(getStackTraceElement().getLineNumber());
    return methodName + " [" + lineNumber + "]" + " -> " + message; 
  }

  public void info(String message) {
    if (logger == null) return;
    logger.info(buildLogMessage(message));
  }

  public void warn(String message) {
    if (logger == null) return;
    logger.warn(buildLogMessage(message));
  }

  public void warn(String message, Throwable throwable) {
    if (logger == null) return;
    logger.warn(buildLogMessage(message), throwable);
  } 

  public void error(String message) {
    if (logger == null) return;
    logger.error(buildLogMessage(message));
  }

  public void error(String message, Throwable throwable) {
    if (logger == null) return;
    logger.error(buildLogMessage(message), throwable);
  }

  public void debug(String message) {
    if (logger == null) return;
    logger.debug(buildLogMessage(message));
  }

  public void debug(String message, Throwable throwable) {
    if (logger == null) return;
    logger.debug(buildLogMessage(message), throwable);
  }
}