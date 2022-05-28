package com.qa.automation.utils.java.utils.os;

public class CommonOsOprs {

    private static final String WINDOWS = "win";
    private static final String MAC = "mac";

    public CommonOsOprs() {
        // Initialize without attributes
    }

    public String getOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public String getPathSeparatorChar() {

        String pathSeparatorChar = null;

        String oSName = getOsName();

        if (oSName.contains(WINDOWS)) {
            pathSeparatorChar = "\\";
        } else if (oSName.contains(MAC)) {
            pathSeparatorChar = "/";
        }

        return pathSeparatorChar;
    }
}
