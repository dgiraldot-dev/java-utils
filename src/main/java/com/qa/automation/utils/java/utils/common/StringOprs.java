package com.qa.automation.utils.java.utils.common;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringOprs {

    public StringOprs() {
        // Initialize without attributes
    }

    public List<String> getStringListWithRegex(String regex, String text, boolean repeatString) {
        return getStringListWithRegex(regex, text, repeatString, true);
    }

    public List<String> getStringListWithRegex(String regex, String text, boolean repeatString, boolean caseSensitive) {
        List<String> matches = new ArrayList<>();
        Matcher matcher;
        if (caseSensitive) {
            matcher = Pattern.compile(regex).matcher(text);
        } else {
            matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        }

        while (matcher.find()) {
            if (repeatString) {
                matches.add(matcher.group());
            } else {
                if (!matches.contains(matcher.group())) matches.add(matcher.group());
            }

        }
        return matches;
    }

    public boolean evaluateRegex(String regex, String text, boolean caseSensitive) {
        boolean response = false;

        String regexResult = getStringWithRegex(regex, text, caseSensitive);
        if (!isEmptyOrNull(regexResult)) {
            response = true;
        }

        return response;
    }

    public boolean evaluateRegex(String regex, String text) {
        return evaluateRegex(regex, text, true);
    }

    public String getStringWithRegex(String regex, String text, boolean caseSensitive) {
        Matcher matcher;
        if (caseSensitive) {
            matcher = Pattern.compile(regex).matcher(text);
        } else {
            matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        }

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    public String getStringWithRegex(String regex, String text) {
        return getStringWithRegex(regex, text, true);
    }

    /**
     * Recortar un String por la derecha dado una cantidad máxima de caracteres (Longitud)
     *
     * @param string          String a recortar
     * @param maxStringLength cantidad máxima de caracteres (Longitud)
     * @return String recortado
     */
    public String cutString(String string, int maxStringLength) {
        if (string.length() > maxStringLength) {
            string = string.substring(0, maxStringLength);
        }

        return string;
    }

    public boolean isNull(String string) {
        boolean respone = false;

        if ((string == null) || string.equalsIgnoreCase("null") || string.equalsIgnoreCase("nulo")) {
            respone = true;
        }

        return respone;
    }

    /**
     * Validar si un String es nulo o vacio
     *
     * @param string String a validar
     * @return true o false
     */
    public boolean isEmptyOrNull(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Poner en minúscula el primer caracter de un String
     *
     * @param string String a modificar
     * @return String string modificado
     */
    public String firstCharToLowerCase(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1, string.length());
    }

    public String firstCharToUpperCase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1, string.length()).toLowerCase();
    }

    public String removeAllSpaces(String string) {
        return string.replaceAll("\\s*", "");
    }

    public String[] splitWithoutWhitespaces(String stringToSplit, String stringUsedToSplit) {
        String[] arraySplit = {};

        if (!isEmptyOrNull(stringToSplit)) {
            arraySplit = stringToSplit.replaceAll("\\s*" + stringUsedToSplit + "\\s*", stringUsedToSplit).split(stringUsedToSplit);
        }

        return arraySplit;
    }

    public int getCountOccurrences(String string, String substring) {
        int cant = 0;
        int indexOf;

        while ((indexOf = string.indexOf(substring)) > -1) {
            string = string.substring(indexOf + substring.length(), string.length());

            cant++;
        }

        return cant;
    }

    public String getStringBetweenTwoStrings(String string, String leftString, String rigthString) {
        int beginIndex = string.lastIndexOf(leftString) + leftString.length();
        int endIndex = string.indexOf(rigthString);

        return string.substring(beginIndex, endIndex);
    }

    public String reverseSplit(String[] array, String stringUsedToSplit) {
        String stringToSplit = "";

        int arrayLength = array.length;
        boolean flagCharSplit = false;

        if (arrayLength > 0) {
            for (int i = 0; i < arrayLength; i++) {
                if (!flagCharSplit) {
                    stringToSplit = array[i];
                    flagCharSplit = true;
                } else {
                    stringToSplit = new StringBuilder(stringToSplit).append(stringUsedToSplit).append(array[i]).toString();
                }
            }
        }

        return stringToSplit;
    }

    public String wrapText(String originalText, int charsPerLine) {
        String retval = "";
        int charCounter = 0;
        boolean insertRc = false;

        if (originalText != null) {

            int len = originalText.length();

            for (int i = 0; i < len; i++) {

                retval = new StringBuilder(retval).append(originalText.charAt(i)).toString();
                charCounter++;

                if ((insertRc) && (originalText.charAt(i) == ' ')) {
                    retval = new StringBuilder(retval).append("\n").toString();
                    insertRc = false;
                }

                if (charCounter > charsPerLine) {
                    charCounter = 0;
                    insertRc = true;
                }
            }
        }

        return retval;
    }

    /***
     * Verifica si el texto es numérico
     *
     * @param string String a comprobar
     * @return true o false
     */

    public Boolean isNumeric(String string) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(string, pos);
        return string.length() == pos.getIndex();
    }

    public String getStringUntilFirstWhitespace(String string) {

        int endIndex = findFirstWhitespace(string);

        if (endIndex == -1) {
            return string;
        } else {
            return string.substring(0, endIndex);
        }
    }

    public int findFirstWhitespace(String string) {
        for (int index = 0; index < string.length(); index++) {
            if (Character.isWhitespace(string.charAt(index))) {
                return index;
            }
        }
        return -1;
    }

    public boolean isUrlValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long stringToLong(String str) {
        return Long.parseLong(str);
    }

    public int stringToInteger(String str) {
        return Integer.parseInt(str);
    }

    public String leftPad(String str, int size, String padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }

    public String leftPad(int number, int size, String padChar) {
        return leftPad(String.valueOf(number), size, padChar);
    }

    public String rightPad(String str, int size, String padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }

    public String rightPad(int number, int size, String padChar) {
        return rightPad(String.valueOf(number), size, padChar);
    }
}