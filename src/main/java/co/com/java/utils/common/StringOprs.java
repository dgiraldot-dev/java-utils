package co.com.java.utils.common;

import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class StringOprs {

  public StringOprs() {

  }

  public List<String> getStringListWithRegex(String regex, String text, boolean repeatString) {
    return getStringListWithRegex(regex, text, repeatString, true);
  }

  public List<String> getStringListWithRegex(String regex, String text, boolean repeatString, boolean caseSensitive) {
    List<String> matches = new ArrayList<String>();
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
    String regexResult = getStringWithRegex(regex, text, caseSensitive);
    if (isEmptyOrNull(regexResult)) {
      return false;
    } else {
      return true;
    }
  }

  public boolean evaluateRegex(String regex, String text) {
    return evaluateRegex(regex, text, true);
  }

  /**
   * Obtener un String de un texto mediante una expresión regular
   * 
   * @param regex expresión regular (substring)
   * @param text texto a validar 
   * 
   * @return String a validar si se cumple la expresión regular o null si no se cumple
   */
  public String getStringWithRegex(String regex, String text, boolean caseSensitive)
  {		
    Matcher matcher;
    if (caseSensitive) {
      matcher = Pattern.compile(regex).matcher(text);
    } else {
      matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
    }

    if (matcher.find())
    {
      return matcher.group();
    }
    else
    {
      return "";
    }
  }

  public String getStringWithRegex(String regex, String text)
  {
    return getStringWithRegex(regex, text, true);
  }

  /**
   * Recortar un String por la derecha dado una cantidad máxima de caracteres (Longitud)
   * 
   * @param string String a recortar
   * @param maxStringLength cantidad máxima de caracteres (Longitud) 
   * 
   * @return String recortado
   */
  public String cutString(String string, int maxStringLength)
  {
    if (string.length() > maxStringLength)
    {
      string = string.substring(0, maxStringLength);
    }

    return string;
  }

  public boolean isNull(String string)
  {		
    if ((string == null) || string.equalsIgnoreCase("null") || string.equalsIgnoreCase("nulo"))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Validar si un String es nulo o vacio
   * 
   * @param string String a validar
   * 
   * @return true o false
   */
  public boolean isEmptyOrNull(String string)
  {
    return string == null || string.isEmpty();
  }

  /**
   * Poner en minúscula el primer caracter de un String
   * 
   * @param string String a modificar
   * 
   * @return String string modificado
   */
  public String firstCharToLowerCase(String string)
  {
    return string.substring(0, 1).toLowerCase() + string.substring(1, string.length());
  }

  public String firstCharToUpperCase(String string)
  {
    return string.substring(0, 1).toUpperCase() + string.substring(1, string.length()).toLowerCase();
  }

  public String removeAllSpaces(String string)
  {
    return string.replaceAll("\\s*", "");
  }

  public String[] splitWithoutWhitespaces(String stringToSplit, String stringUsedToSplit)
  {
    String arraySplit[] = {};

    if (!isEmptyOrNull(stringToSplit))
    {
      arraySplit = stringToSplit.replaceAll("\\s*" + stringUsedToSplit + "\\s*", stringUsedToSplit).split(stringUsedToSplit);
    }

    return arraySplit;
  }	

  public int getCountOccurrences(String string, String substring)	 
  {
    int cant = 0;		
    int indexOf;

    while ((indexOf = string.indexOf(substring)) > -1)
    {
      string = string.substring(indexOf + substring.length(), string.length());

      cant++;
    }   

    return cant;  
  }

  public String getStringBetweenTwoStrings(String string, String leftString, String rigthString)
  {
    int beginIndex = string.lastIndexOf(leftString) + leftString.length();
    int endIndex = string.indexOf(rigthString);

    return string.substring(beginIndex, endIndex);
  }

  public String reverseSplit(String[] array, String stringUsedToSplit)
  {
    String stringToSplit = "";

    int arrayLength = array.length;
    boolean flagCharSplit = false;

    if (arrayLength > 0)
    {
      for (int i = 0; i < arrayLength; i++)
      {
        if (!flagCharSplit)
        {
          stringToSplit = array[i];					
          flagCharSplit = true;
        }
        else
        {
          stringToSplit = stringToSplit + stringUsedToSplit + array[i];
        }
      }
    }

    return stringToSplit;
  }

  /***
   * Hace el Wrap del texto de entrada, retornando un nuevo texto con saltos de línea según el número
   * de carcateres especificado en el parámetro chars_per_line.
   * 
   * @param original_text   Texto de entrada
   * @param chars_per_line  numero de caracteres deseados por línea
   * @return
   */

  public String wrapText(String original_text, int chars_per_line)
  {
    String    retval = "";
    int 	  char_counter = 0;
    boolean   insert_rc = false;

    if(original_text != null) {

      int len = original_text.length();

      for(int i = 0; i < len; i++) {

        retval += original_text.charAt(i);
        char_counter++;

        if(insert_rc) {

          if(original_text.charAt(i) == ' ') {
            retval += "\n";
            insert_rc = false;
          }

        }

        if(char_counter > chars_per_line) {
          char_counter = 0;
          insert_rc = true;
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
    } else
    {
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

  public boolean isUrlValid(String url) 
  { 
    try { 
      new URL(url).toURI(); 
      return true; 
    }	          
    catch (Exception e) { 
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