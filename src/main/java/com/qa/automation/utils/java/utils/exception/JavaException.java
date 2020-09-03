package com.qa.automation.utils.java.utils.exception;

import com.qa.automation.utils.java.utils.common.DialogWindowOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.logger.LogManager;
import com.qa.automation.utils.java.utils.params.JavaUtilsParams;

public class JavaException {

  private static final LogManager LOGGER = new LogManager();

  private StringOprs stringOprs = new StringOprs();

  public JavaException() {

  }

  public void throwException(String message, Throwable e, Boolean endSystemRun) {
    boolean showErrorMessageDialogByThrowException = JavaUtilsParams.SHOW_ERROR_MESSAGE_DIALOG_BY_THROW_EXCEPTION;
    boolean endSystemRunByThrowException = (boolean) JavaUtilsParams.END_SYSTEM_RUN_BY_THROW_EXCEPTION;

    String systemRunCompletionMessage = "";
    
    if ((endSystemRun != null) && (endSystemRun == true)) {
      endSystemRunByThrowException = true;
    } else if ((endSystemRun != null) && (endSystemRun == false)) {
      endSystemRunByThrowException = false;
    }

    if (endSystemRunByThrowException) {
      systemRunCompletionMessage = "La ejecución ha finalizado (System.exit(1))...";
    } else {
      systemRunCompletionMessage = "La ejecución continuará...";
    }

    StackTraceElement stackTraceElement = LOGGER.getStackTraceElement();

    if (stackTraceElement == null) {
      //System.out.println(systemRunCompletionMessage);
      e.printStackTrace();
    } else {
      String className = stackTraceElement.getClassName();
      String methodName = stackTraceElement.getMethodName();
      int lineNumber = stackTraceElement.getLineNumber();

      if (!stringOprs.isEmptyOrNull(message)) {
        LOGGER.error(message, e);
        //System.out.println(message);
        e.printStackTrace();
        message = message + "\r\n\r\n";
      } else {
        LOGGER.error("*** THROW EXCEPTION ***", e);
        e.printStackTrace();
        message = "";
      }

      LOGGER.info(systemRunCompletionMessage);
      //System.out.println(systemRunCompletionMessage);

      if (showErrorMessageDialogByThrowException) {
        String exceptionMessage = e.getMessage();

        if (!stringOprs.isEmptyOrNull(exceptionMessage)) {
          exceptionMessage = e.getMessage() + "\r\n\r\n";
        } else {
          exceptionMessage = "";
        }

        String messageDialog = "THROW EXCEPTION (" + Thread.currentThread().getName() + ")" + "\r\n\r\n" + message + "Clase: " + className + "\r\n" + "Metodo: " + methodName + "\r\n" + "Linea: " + lineNumber + "\r\n\r\n" + exceptionMessage + systemRunCompletionMessage;

        new DialogWindowOprs().showErrorMessageDialog(messageDialog);  
      }
    }

    if (endSystemRunByThrowException) {
      System.exit(1); 
    }
  }
  
  public void throwException(Throwable e, Boolean endSystemRun) {
    throwException(null, e, endSystemRun);
  }

  public void throwException(Throwable e) {
    throwException(e, null);
  }
  
  public void throwException(String message, Boolean endSystemRun){
    throwException(new Exception(message), endSystemRun);
  }

  public void throwException(String message){
    throwException(message, null);
  }

  public void catchException(String message, Throwable e) {
    boolean showWarnMessageDialogByCatchException = JavaUtilsParams.SHOW_WARN_MESSAGE_DIALOG_BY_CATCH_EXCEPTION;
    StackTraceElement stackTraceElement = LOGGER.getStackTraceElement();

    if (stackTraceElement == null) {
      e.printStackTrace();
    } else {

      String className = stackTraceElement.getClassName();
      String methodName = stackTraceElement.getMethodName();
      int lineNumber = stackTraceElement.getLineNumber();

      if (!stringOprs.isEmptyOrNull(message)) {
        LOGGER.warn(message, e);
        //System.out.println(message);
        e.printStackTrace();
        message = message + "\r\n\r\n";
      } else {
        LOGGER.warn("*** CATCH EXCEPTION ***", e);
        e.printStackTrace();
        message = "";
      }

      if (showWarnMessageDialogByCatchException) {
        String exceptionMessage = e.getMessage();

        if (!stringOprs.isEmptyOrNull(exceptionMessage)) {
          exceptionMessage = e.getMessage() + "\r\n\r\n";
        } else {
          exceptionMessage = "";
        }

        String messageDialog = "CATCH EXCEPTION (" + Thread.currentThread().getName() + ")" + "\r\n\r\n" + message + "Clase: " + className + "\r\n" + "Metodo: " + methodName + "\r\n" + "Linea: " + lineNumber + "\r\n\r\n" + exceptionMessage;

        new DialogWindowOprs().showWarnMessageDialog(messageDialog);  
      }
    }
  }

  public void catchException(Throwable e) {
    catchException(null, e);
  }

  public void catchException(String message){
    catchException(new Exception(message));
  }
}