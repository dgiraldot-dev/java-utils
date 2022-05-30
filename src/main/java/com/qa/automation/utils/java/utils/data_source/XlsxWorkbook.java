/**
 * Grupo Aval Acciones y Valores S.A. CONFIDENTIAL
 *
 * <p>Copyright (c) 2018 . All Rights Reserved.
 *
 * <p>NOTICE: This file is subject to the terms and conditions defined in file 'LICENSE', which is
 * part of this source code package.
 */
package com.qa.automation.utils.java.utils.data_source;

import com.qa.automation.utils.java.utils.exception.GenericRuntimeException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

public class XlsxWorkbook {

    private String xlsxFilePath;

    public XlsxWorkbook(String xlsxFilePath) {
        this.xlsxFilePath = xlsxFilePath;
    }

    public Workbook getWorkbook() {
        try {
            return new XSSFWorkbook(new FileInputStream(new File(xlsxFilePath)));
        } catch (Exception e) {
            throw new GenericRuntimeException(e);
        }
    }
}