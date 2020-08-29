package com.qa.automation.utils.java.utils;

import java.util.concurrent.TimeUnit;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;

public class JavaOprs {

  public JavaOprs() {

  }

  public void sleepInSeconds(long seconds) {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void sleepInMilliseconds(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void runGarbageColletor() {
    System.gc();
    Runtime.getRuntime().gc();
  }

  public Boolean CheckIfExistClassPath(String classPath) {		
    try {
      Class.forName(classPath);
      return true;
    } catch( ClassNotFoundException e ) {
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

    String ClassNameAcronym = null;
    String[] packageNames = null;
    int packageNamesLenght = 0;

    if (!stringOprs.isEmptyOrNull(fullClassName)) {
      packageNames = fullClassName.split("\\.");
      packageNamesLenght = packageNames.length;
    }

    for (int i = 0; i < packageNamesLenght - 1; i++) {
      if (i == 0) {
        ClassNameAcronym = packageNames[i].substring(0, 1);
      } else {
        ClassNameAcronym = ClassNameAcronym + "." + packageNames[i].substring(0, 1);
      }
    }

    return ClassNameAcronym;	
  }
}