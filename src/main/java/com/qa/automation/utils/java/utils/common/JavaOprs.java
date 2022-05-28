package com.qa.automation.utils.java.utils.common;

import java.util.concurrent.TimeUnit;

public class JavaOprs {

    public JavaOprs() {
        // Initialize without attributes
    }

    public void sleepInSeconds(long seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void sleepInMilliseconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void runGarbageColletor() {
        System.gc();
        Runtime.getRuntime().gc();
    }

    public Boolean checkIfExistClassPath(String classPath) {
        try {
            Class.forName(classPath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public String getThisProjectDirectoryPath() {
        return System.getProperty("user.dir");
    }

    public String getThisProjectParentDirectoryPath() {
        return new FileOprs().getParentDirectoryPathFromFilePath(getThisProjectDirectoryPath());
    }

    public String findFileBackwardsAndGetAbsoluteFilePath(String fileName) {
        return new FileOprs().findFileBackwardsAndGetAbsoluteFilePath(getThisProjectDirectoryPath(), fileName);
    }

    public String getClassPackageNameAcronym(String fullClassName) {
        StringOprs stringOprs = new StringOprs();

        String classNameAcronym = null;
        String[] packageNames = null;
        int packageNamesLenght = 0;

        if (!stringOprs.isEmptyOrNull(fullClassName)) {
            packageNames = fullClassName.split("\\.");
            packageNamesLenght = packageNames.length;
        }

        if (packageNames != null) {
            for (int i = 0; i < packageNamesLenght - 1; i++) {
                if (i == 0) {
                    classNameAcronym = packageNames[i].substring(0, 1);
                } else {
                    classNameAcronym = new StringBuilder(classNameAcronym).append(".").append(packageNames[i].substring(0, 1)).toString();
                }
            }
        }

        return classNameAcronym;
    }
}