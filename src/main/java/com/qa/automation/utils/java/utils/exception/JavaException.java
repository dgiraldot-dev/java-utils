package com.qa.automation.utils.java.utils.exception;

import com.qa.automation.utils.java.utils.common.DialogWindowOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.logger.LogManager;
import com.qa.automation.utils.java.utils.params.JavaUtilsParams;

public class JavaException {

    private static final LogManager LOGGER = new LogManager();
    private static final String DOUBLE_PAGE_BREAK = "\r\n\r\n";
    private StringOprs stringOprs = new StringOprs();

    public JavaException() {
        // Initialize without attributes
    }

    public void throwException(String message, Throwable e, Boolean endSystemRun) {
        boolean showErrorMessageDialogByThrowException = JavaUtilsParams.SHOW_ERROR_MESSAGE_DIALOG_BY_THROW_EXCEPTION;
        boolean endSystemRunByThrowException = JavaUtilsParams.END_SYSTEM_RUN_BY_THROW_EXCEPTION;

        String systemRunCompletionMessage = "";

        if (endSystemRun != null) {
            if (endSystemRun) {
                endSystemRunByThrowException = true;
            } else {
                endSystemRunByThrowException = false;
            }
        }

        if (endSystemRunByThrowException) {
            systemRunCompletionMessage = "La ejecución ha finalizado (System.exit(1))...";
        } else {
            systemRunCompletionMessage = "La ejecución continuará...";
        }

        StackTraceElement stackTraceElement = LOGGER.getStackTraceElement();

        if (stackTraceElement == null) {
            e.printStackTrace();
        } else {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            int lineNumber = stackTraceElement.getLineNumber();

            if (!stringOprs.isEmptyOrNull(message)) {
                LOGGER.error(message, e);
                e.printStackTrace();
                message = message + DOUBLE_PAGE_BREAK;
            } else {
                LOGGER.error("*** THROW EXCEPTION ***", e);
                e.printStackTrace();
                message = "";
            }

            LOGGER.info(systemRunCompletionMessage);

            if (showErrorMessageDialogByThrowException) {
                String exceptionMessage = e.getMessage();

                if (!stringOprs.isEmptyOrNull(exceptionMessage)) {
                    exceptionMessage = e.getMessage() + DOUBLE_PAGE_BREAK;
                } else {
                    exceptionMessage = "";
                }

                String messageDialog = "THROW EXCEPTION (" + Thread.currentThread().getName() + ")" + DOUBLE_PAGE_BREAK + message + "Clase: " + className + "\r\n" + "Metodo: " + methodName + "\r\n" + "Linea: " + lineNumber + DOUBLE_PAGE_BREAK + exceptionMessage + systemRunCompletionMessage;

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

    public void throwException(String message, Boolean endSystemRun) {
        throwException(new Exception(message), endSystemRun);
    }

    public void throwException(String message) {
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
                e.printStackTrace();
                message = message + DOUBLE_PAGE_BREAK;
            } else {
                LOGGER.warn("*** CATCH EXCEPTION ***", e);
                e.printStackTrace();
                message = "";
            }

            if (showWarnMessageDialogByCatchException) {
                String exceptionMessage = e.getMessage();

                if (!stringOprs.isEmptyOrNull(exceptionMessage)) {
                    exceptionMessage = e.getMessage() + DOUBLE_PAGE_BREAK;
                } else {
                    exceptionMessage = "";
                }

                String messageDialog = "CATCH EXCEPTION (" + Thread.currentThread().getName() + ")" + DOUBLE_PAGE_BREAK + message + "Clase: " + className + "\r\n" + "Metodo: " + methodName + "\r\n" + "Linea: " + lineNumber + DOUBLE_PAGE_BREAK + exceptionMessage;

                new DialogWindowOprs().showWarnMessageDialog(messageDialog);
            }
        }
    }

    public void catchException(Throwable e) {
        catchException(null, e);
    }

    public void catchException(String message) {
        catchException(new Exception(message));
    }
}