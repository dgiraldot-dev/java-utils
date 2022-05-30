package com.qa.automation.utils.java.utils.common;

import javax.swing.*;

public class DialogWindowOprs {
    private static final String INFORMACION = "Información";
    private static final String ADVERTENCIA = "Advertencia";
    private static final String ERROR = "Error";
    private static final String CONFIRMACION = "Confirmación";
    private static final String VALOR_ENTRADA = "Valor de Entrada";

    public DialogWindowOprs() {
        // Initialize without attributes
    }

    public void showInfoMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, INFORMACION, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showInfoMessageDialogInThread(String message) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                showInfoMessageDialog(message);
            }
        });
        thread.start();
    }

    public void showWarnMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, ADVERTENCIA, JOptionPane.WARNING_MESSAGE);
    }

    public void showWarnMessageDialogInThread(String message) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                showWarnMessageDialog(message);
            }
        });
        thread.start();

    }

    public void showErrorMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public void showErrorMessageDialogInThread(String message) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                showErrorMessageDialog(message);
            }
        });
        thread.start();

    }

    public Boolean showConfirmDialog(String message) {
        Boolean response = false;

        int result = JOptionPane.showConfirmDialog(null, message, CONFIRMACION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == 0) {
            response = true;
        }

        return response;
    }

    public String showInputDialog(String message) {
        String response = null;

        response = JOptionPane.showInputDialog(null, message, VALOR_ENTRADA, JOptionPane.DEFAULT_OPTION);

        return response;
    }

    public String showInputDialogWithSelectOptions(String message, String[] options, String initialOption) {
        String response = null;

        response = (String) JOptionPane.showInputDialog(null, message, VALOR_ENTRADA, JOptionPane.DEFAULT_OPTION, null, options, initialOption);

        return response;
    }
}