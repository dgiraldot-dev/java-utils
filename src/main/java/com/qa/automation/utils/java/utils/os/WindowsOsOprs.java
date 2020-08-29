package com.qa.automation.utils.java.utils.os;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import com.qa.automation.utils.java.utils.JavaOprs;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.common.StringOprs;
import com.qa.automation.utils.java.utils.exception.JavaException;

public class WindowsOsOprs {

  public WindowsOsOprs() {

  }

  public void killSystemProcessWhitTaskKillWindowsCommand(String systemProcessName) {    
    executeWindowsCommand("taskkill /f /im " + systemProcessName);        
  }

  public String killSystemProcessWhitTaskKillWindowsCommandAndWaitForItsDeath(String systemProcessName) {    
    return executeWindowsCommandAndWaitForItsDeath("taskkill /f /im " + systemProcessName);
  }

  public void runBatchFile(String batchFilePath) {
    String command = "\"" + batchFilePath + "\"";
    executeWindowsCommandAndWaitForItsDeath(command);		
  }

  public Process executeWindowsCommand(String command) {		
    try {
      return Runtime.getRuntime().exec(command);
    } catch (IOException e) {
      new JavaException().catchException(e);
      return null;
    }
  }

  public String executeWindowsCommandAndWaitForItsDeath(String command) {
    String processInputStream = null;
    ProcessBuilder processBuilder = null;
    Process process = null;

    try {
      processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
      process = processBuilder.start();
      process.waitFor();
      processInputStream = getProcessInputStream(process.getInputStream());
    } catch (IOException e) {
      new JavaException().catchException(e);
    } catch (InterruptedException e) {
      new JavaException().catchException(e);
    }

    return processInputStream;
  }

  private String getProcessInputStream(InputStream inputStream)
  {
    int bytesRead = -1;
    byte[] bytes = new byte[1024];
    String output = null;
    try {
      while((bytesRead = inputStream.read(bytes)) > -1){
        output = output + new String(bytes, 0, bytesRead);
      }
    } catch (IOException e) {
      new JavaException().catchException(e);
    }
    return output;
  }

  public String getCurrentUserName() {
    return System.getProperty("user.name");
  }

  public String getCurrentHostName() {
    String hostName = null;
    try {
      hostName = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      new JavaException().catchException(e);
    }

    return hostName;
  }

  public boolean isProcessRunning(String processName)  {
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader br = null;

    List<String> command = new ArrayList<String>();
    command.add("WMIC");
    command.add("process");
    command.add("list");
    command.add("brief");

    try {
      ProcessBuilder builder = new ProcessBuilder(command);
      Process process = builder.start();
      is = process.getInputStream();
      isr = new InputStreamReader(is);
      br = new BufferedReader(isr);
      String line;
      processName = processName.toUpperCase();
      while ((line = br.readLine()) != null) {
        if (line.toUpperCase().indexOf(processName) > -1) return true;
      }
      return false;
    } catch (IOException e) {
      return false;
    } finally {
      try {
        if (br != null) br.close();
        if (isr != null) isr.close();
        if (is != null) is.close();
      } catch (IOException e) {

      }
    }
  }

  public void printAllOpenSystemProcesses() {
    try {
      String line;
      Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
      BufferedReader input =
          new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null) {
        System.out.println(line); //<-- Parse data here.
      }
      input.close();
    } catch (Exception e) {
      new JavaException().catchException(e);
    }
  }

  public void waitUntilSystemProcessStarts(String processName, int seconds) {

    int contador = 0;

    try {
      while (!isProcessRunning(processName) && (contador <= seconds)) {
        new JavaOprs().sleepInSeconds(1);
        contador = contador + 1;
      }
    } catch (Exception e) {
      new JavaException().catchException(e);
    }
  }

  public void waitUntilSystemProcessIsFinished(String processName, int seconds) {

    int contador = 0;

    try {
      while (isProcessRunning(processName) && (contador <= seconds)) {
        new JavaOprs().sleepInSeconds(1);
        contador = contador + 1;
      }
    } catch (Exception e) {

    }
  }

  public void releaseSystemPort(String systemPort) {

    StringOprs stringOprs = new StringOprs();

    String commandResponse = executeWindowsCommandAndWaitForItsDeath("netstat -ano | findstr :" + systemPort);
    String processId = null;

    if (commandResponse != null) {
      processId = stringOprs.getStringWithRegex("[0-9]+", stringOprs.getStringWithRegex("LISTENING.*[0-9]+", commandResponse, false), false);
    }

    if (processId != null) {
      executeWindowsCommandAndWaitForItsDeath("taskkill /pid " + processId  + " /f");
    }
  }

  public String findAndGetFreePort() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(0);
      socket.setReuseAddress(true);
      int systemPort = socket.getLocalPort();
      try {
        socket.close();
      } catch (IOException e) {}
      releaseSystemPort(String.valueOf(systemPort));
      return String.valueOf(systemPort);
    } catch (IOException e) {
    } finally {
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {}
      }
    }

    throw new IllegalStateException("Could not find a free TCP/IP System Port");
  }

  public void openTextFileInWindowsNotepad(String filePath) {
    if (new FileOprs().existsFile(filePath)) {
      executeWindowsCommand("cmd.exe /s /c start /max notepad \"" + filePath + "\"");
    } 
  }

  public void openWindowsExplorerAndSelectFile(String filePath) {
    if (new FileOprs().existsFile(filePath)) {
      executeWindowsCommand("explorer.exe /select," + filePath);
    } 
  }
}