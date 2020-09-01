package com.qa.automation.utils.java.utils.common;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import com.qa.automation.utils.java.utils.params.CommonParams;

public class FileOprs extends FileUtils {
  
  public FileOprs() {

  }

  public String addBackslashToEndOfDirectoryPath(String directoryPath) {

    StringOprs stringOprs = new StringOprs();

    if ((!stringOprs.isEmptyOrNull(directoryPath)) && (!directoryPath.endsWith(CommonParams.PATH_SEPARATOR_CHAR))) {
      directoryPath = directoryPath + CommonParams.PATH_SEPARATOR_CHAR;
    }
    return directoryPath;
  }

  public String removeBackslashToEndOfDirectoryPath(String directoryPath) {

    StringOprs stringOprs = new StringOprs();

    if ((!stringOprs.isEmptyOrNull(directoryPath)) && (directoryPath.endsWith(CommonParams.PATH_SEPARATOR_CHAR))) {
      directoryPath = directoryPath.substring( 0, directoryPath.lastIndexOf( CommonParams.PATH_SEPARATOR_CHAR ) );
    }
    return directoryPath;
  }

  public String findFileBackwardsAndGetAbsoluteFilePath(String pivotDirectoryPath, String fileName) {

    StringOprs stringOprs = new StringOprs();

    String absoluteFilePath = null;

    if (!stringOprs.isEmptyOrNull(pivotDirectoryPath) && !stringOprs.isEmptyOrNull(fileName)) {
      while (!stringOprs.isEmptyOrNull(pivotDirectoryPath)) {
        if (existsFile(normalizePath(pivotDirectoryPath, fileName))) {
          if (isDirectory(fileName)) {
            absoluteFilePath = addBackslashToEndOfDirectoryPath(normalizePath(pivotDirectoryPath, fileName));
          } else {
            absoluteFilePath = normalizePath(pivotDirectoryPath, fileName);
          }
          break;
        } else {
          pivotDirectoryPath = getParentDirectoryPathFromFilePath(pivotDirectoryPath);
        }
      }
    }
    return absoluteFilePath;		
  }
  
  public String findFileInThisProjectAndGetAbsoluteFilePath(String fileNameWithExtension) {
    return findFileAndGetAbsoluteFilePath(System.getProperty("user.dir"), fileNameWithExtension);
  }

  public String findFileAndGetAbsoluteFilePath(String searchDirectoryPath, String fileNameWithExtension) {

    StringOprs stringOprs = new StringOprs();

    String subdirectory = getDirectoryPathFromFilePath(fileNameWithExtension);
    fileNameWithExtension = getFileNameWithExtensionFromFilePath(fileNameWithExtension);
    String absoluteFilePath = null;

    if (searchDirectoryPath != null) {

      File sourceDirectoryFile = new File(searchDirectoryPath);

      try {
        Collection<?> files = FileUtils.listFiles(sourceDirectoryFile, null, true);

        for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
          File file = (File) iterator.next();
          if (file.getName().equals(fileNameWithExtension)) {
            absoluteFilePath = file.getAbsolutePath();
            if ((stringOprs.isEmptyOrNull(subdirectory)) || (absoluteFilePath.contains(normalizePath(subdirectory, fileNameWithExtension)))) {							
              break;
            } else {
              absoluteFilePath = null;
            }
          }
        }
      } catch (Exception e) {
        //new JavaException().catchException(e);
      }
    }

    return absoluteFilePath;
  }

  /**
   * Buscar directorio en un directorio fuente 
   *
   * @param searchDirectoryPath directorio fuente en el cual se va a realizar la busqueda
   * @param directoryName nombre del directorio a buscar
   * @return ruta absoluta del directorio encontrado  
   */
  public String findDirectoryAndGetAbsoluteDirectoryPath(String searchDirectoryPath, String directoryName) {

    StringOprs stringOprs = new StringOprs();

    String subdirectory = getDirectoryPathFromFilePath(directoryName);
    directoryName = getFileNameWithExtensionFromFilePath(directoryName);
    String absoluteDirectoryPath = null;

    if (searchDirectoryPath != null) {		

      File sourceDirectoryFile = new File(searchDirectoryPath);

      try {
        Collection<?> files = FileUtils.listFilesAndDirs(sourceDirectoryFile, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);

        for (Iterator<?> iterator = files.iterator(); iterator.hasNext();) {
          File file = (File) iterator.next();
          if (file.getName().equals(directoryName)) {
            absoluteDirectoryPath = file.getAbsolutePath();
            if ((stringOprs.isEmptyOrNull(subdirectory)) || (absoluteDirectoryPath.contains(removeBackslashToEndOfDirectoryPath(normalizePath(subdirectory, directoryName))))) {							
              break;
            } else {
              absoluteDirectoryPath = null;
            }
          }
        }
      } catch (Exception e) {
        //new JavaException().catchException(e);
      }
    }

    return absoluteDirectoryPath;
  }

  public String getFileContent ( String filePath ) {
    File file = new File( filePath );

    try {
      return FileUtils.readFileToString( file, Charset.defaultCharset() );
    } catch (IOException e) {
      //new JavaException().catchException(e);
      return null;
    }
  }

  public String getFileContent ( String filePath, Charset encoding ) {
    File file = new File( filePath );

    try {
      return FileUtils.readFileToString(file, encoding);
    } catch (IOException e) {
      //new JavaException().catchException(e);
      return null;
    }
  }

  public String getFileContent (String filePath, String encoding) {
    File file = new File( filePath );

    try {
      return FileUtils.readFileToString(file, encoding);
    } catch (IOException e) {
      //new JavaException().catchException(e);
      return null;
    }
  }

  public String replaceStringInFileContent(String filePath, String regex, String replacement, String encoding) {
    return getFileContent(filePath, encoding).replaceAll(regex, replacement);
  }

  public String replaceStringInFileContent(String filePath, String regex, String replacement, Charset encoding) {
    return getFileContent(filePath, encoding).replaceAll(regex, replacement);
  }

  public String replaceStringInFileContent(String filePath, String regex, String replacement) {
    return getFileContent(filePath).replaceAll(regex, replacement);
  }

  /**
   * Escribir en un arhivo de texto
   * @param filePath ruta del archivo de texto
   * @param content contenido a escribir en el archivo de texto
   */
  public void writeFileContent ( String filePath, String content ) {
    writeFileContent( filePath, content, false );
  }

  /**
   * Escribir en un arhivo de texto
   * @param filePath ruta del archivo de texto
   * @param content contenido a escribir en el archivo de texto
   * @param append adicionar el nuevo contenido al contenido actual del archivo?
   */
  public void writeFileContent ( String filePath, String content, boolean append ) {
    try {
      File file = new File( filePath );

      createParentDirectory( filePath );

      BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter( file, append ) );
      bufferedWriter.write( content );
      bufferedWriter.flush();
      bufferedWriter.close();

      bufferedWriter = null;
      file = null;
    } catch ( Exception e ) {
      //new JavaException().catchException(e);
    }
  }

  /**
   * Generar correctamente (Normalizar separadores) la ruta de un archivo, dada la parte izquierda (Inicial) y derecha
   * (Final) de la ruta del archivo
   * @param leftPath parte izquierda (inicial) de la ruta del archivo (Ejemplo: C:\Temporal\)
   * @param rightPath parte derecha (final) de la ruta del archivo (Ejemplo: \Archivo.ext)
   * @return String con la ruta normalizada del archivo (Ejemplo: C:\Temporal\Archivo.ext)
   */
  public String normalizePath ( String leftPath, String rightPath ) {

    Path path = Paths.get(leftPath + CommonParams.PATH_SEPARATOR_CHAR + rightPath);
    String filePath = path.normalize().toString();

    if (existsFile(filePath)) {
      if (isDirectory(filePath)) {
        filePath = addBackslashToEndOfDirectoryPath(filePath);
      }
    } else {
      if (checkIfPathStringIsDirectoryPath(filePath)) {
        filePath = addBackslashToEndOfDirectoryPath(filePath);
      }
    }

    return filePath;
  }

  /**
   * Comparar el contenido de dos archivos
   * @param filePath01 ruta del archivo 01
   * @param filePath02 ruta del archivo 02
   * @return true o false
   */
  public boolean compareFiles ( String filePath01, String filePath02 ) {
    try {
      File file01 = new File( filePath01 );
      File file02 = new File( filePath02 );

      return FileUtils.contentEquals( file01, file02 );
    } catch ( Exception e ) {
      //new JavaException().catchException(e);
      return false;
    }
  }

  /**
   * Validar si un archivo o directorio determinado existe
   * @param filePath ruta del archivo o directorio
   * @return true o false
   */
  public boolean existsFile ( String filePath ) {
    try {
      File file = new File( filePath );

      if ( file.exists() ) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }
  
  public String checkIfExistsFileAndGetAbsolutePath ( String filePath ) {
    try {
      File file = new File( filePath );

      if ( file.exists() ) {
        return file.getAbsolutePath();
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Crear un directorio
   * @param directoryPath ruta del directorio a crear
   */
  public void createDirectory ( String directoryPath ) {
    File file = new File( directoryPath );

    if ( !file.exists() ) {
      file.mkdirs();
    }

    file = null;
  }

  /**
   * Crear un directorio dada la ruta de un archivo determinado
   * @param filePath ruta del archivo
   */
  public void createParentDirectory ( String filePath ) {
    File file = new File( filePath );
    createDirectory( file.getParent() );

    file = null;
  }

  /**
   * Eliminar un archivo
   * @param filePath ruta del archivo a eliminar
   */
  public void deleteFile ( String filePath ) {
    File file = new File( filePath );

    if ( file.isFile() ) {
      file.delete();
    } else {
      System.out.println( "File type is not file" );
    }
  }

  public boolean isDirectory ( String directoryPath ) {
    File file = new File( directoryPath );

    return file.isDirectory();
  }

  public boolean isDirectoryEmpty ( String directoryPath ) {
    File file = new File( directoryPath );

    return file.list().length == 0;
  }

  public boolean checkIfPathStringIsDirectoryPath ( String directoryPathString ) {
    return !checkIfPathStringIsFilePath(directoryPathString);
  }

  public boolean isFile ( String filePath ) {
    File file = new File( filePath );

    return file.isFile();
  }

  public boolean checkIfPathStringIsFilePath ( String filePathString ) {
    String regex = "(.+)(:" + CommonParams.PATH_SEPARATOR_CHAR + CommonParams.PATH_SEPARATOR_CHAR + ")?(.+)(\\.)([^" + CommonParams.PATH_SEPARATOR_CHAR + CommonParams.PATH_SEPARATOR_CHAR + "]+)";		
    return new StringOprs().evaluateRegex(regex, filePathString, false);
  }	

  /**
   * Eliminar un directorio
   * @param directoryPath ruta del directorio a eliminar
   */
  public void deleteDirectory ( String directoryPath ) {
    try {
      File file = new File( directoryPath );

      if ( file.isDirectory() ) {
        FileUtils.deleteDirectory( file );
      } else {
        System.out.println( "File type is not directory" );
      }
    } catch ( Exception e ) {
      //new JavaException().catchException(e);
    }
  }

  /**
   * Copiar un directorio
   * @param sourceDirectoryPath ruta del directorio origen
   * @param destDirectoryPath ruta del directorio destino
   */
  public void copyDirectory ( String sourceDirectoryPath, String destDirectoryPath ) {
    try {
      File sourceFile = new File( sourceDirectoryPath );
      File destFile = new File( destDirectoryPath );

      FileUtils.copyDirectory( sourceFile, destFile );
    } catch ( Exception e ) {
      //new JavaException().catchException(e);
    }
  }

  /**
   * Cortar un directorio. Una vez copiado el directorio, este es eliminado
   * @param sourceDirectoryPath ruta del directorio origen
   * @param destDirectoryPath ruta del directorio destino
   */
  public void cutDirectory ( String sourceDirectoryPath, String destDirectoryPath ) {
    copyDirectory( sourceDirectoryPath, destDirectoryPath );
    deleteDirectory( sourceDirectoryPath );
  }

  public void copyAllDirectoryFilesToDirectory ( String sourceFilePath, String destDirectoryPath ) {
    try {
      File sourceFile = new File( sourceFilePath );
      File destFile = new File( destDirectoryPath );

      if ( sourceFile.isFile() ) {
        FileUtils.copyFileToDirectory( sourceFile, destFile );
        return;
      }

      File[] sourceFileList = sourceFile.listFiles();

      for ( File srcFile : sourceFileList ) {
        if ( srcFile.isFile() ) {
          FileUtils.copyFileToDirectory( srcFile, destFile );
        } else {
          FileUtils.copyDirectory( srcFile, new File( normalizePath( destDirectoryPath, srcFile.getName() ) ) );
        }
      }
    } catch ( Exception e ) {
      //new JavaException().catchException(e);
    }
  }

  /**
   * Copiar un archivo
   * @param sourceFilePath ruta del archivo origen
   * @param destFilePath ruta del archivo destino
   */
  public void copyFile ( String sourceFilePath, String destFilePath ) {
    try {
      if ( new File( destFilePath ).isDirectory() ) {
        String fileName = getFileNameWithExtensionFromFilePath( sourceFilePath );
        destFilePath = normalizePath( destFilePath, fileName );
      }

      File sourceFile = new File( sourceFilePath );
      File destFile = new File( destFilePath );

      FileUtils.copyFile( sourceFile, destFile );
    } catch ( Exception e ) {
      //new JavaException().catchException(e);
    }
  }

  /**
   * Copiar un archivo de una url
   * @param sourceFilePath ruta del archivo origen
   * @param destFilePath ruta del archivo destino
   */
  public void copyFileFromURL ( String sourceFilePath, String destFilePath ) {
    try {
      new URL( sourceFilePath );
    } catch ( MalformedURLException e ) {
      //new JavaException().catchException(e);
    }
    try {
      FileUtils.copyURLToFile( new URL( sourceFilePath ), new File( destFilePath ) );
    } catch ( IOException e ) {
      //new JavaException().catchException(e);
    }
  }

  /**
   * Cortar un archivo. Una vez copiado el archivo, este es eliminado
   * @param sourceFilePath ruta del archivo origen
   * @param destFilePath ruta del archivo destino
   */
  public void cutFile ( String sourceFilePath, String destFilePath ) {
    copyFile( sourceFilePath, destFilePath );
    deleteFile( sourceFilePath );
  }

  /**
   * Obtener el nombre de archivo (Sin extensión) de una ruta de archivo determinada
   * @param filePath ruta del archivo
   * @return nombre del archivo (Sin extensión)
   */
  public String getFileNameWithoutExtensionFromFilePath ( String filePath ) {
    //return stringOprs.getStringWithRegex( "(.*)", filePath.substring( filePath.lastIndexOf( Parameters.PATH_SEPARATOR_CHAR.get() ) + 1 ) );
    if (filePath.endsWith(CommonParams.PATH_SEPARATOR_CHAR)) {
      filePath = filePath.substring(0, filePath.length() - 1);
    }
    return FilenameUtils.getBaseName(filePath);
  }

  /**
   * Obtener el nombre completo de archivo (Con extensión) de una ruta de archivo determinada
   * @param filePath ruta del archivo
   * @return nombre completo del archivo (Con extensión)
   */
  public String getFileNameWithExtensionFromFilePath ( String filePath ) {
    //return filePath.substring( filePath.lastIndexOf( Parameters.PATH_SEPARATOR_CHAR.get() ) + 1 );
    if (filePath.endsWith(CommonParams.PATH_SEPARATOR_CHAR)) {
      filePath = filePath.substring(0, filePath.length() - 1);
    }
    return FilenameUtils.getName(filePath);
  }

  /**
   * Obtener la extension de archivo de una ruta de archivo determinada
   * @param filePath ruta del archivo
   * @return extension del archivo
   */
  public String getFileExtensionFromFilePath ( String filePath ) {
    //return filePath.substring( filePath.lastIndexOf( "." ) + 1 );
    return FilenameUtils.getExtension(filePath);
  }

  public String getDirectoryPathFromFilePath ( String filePath ) {
    filePath = removeBackslashToEndOfDirectoryPath(filePath);
    if (filePath.startsWith(CommonParams.PATH_SEPARATOR_CHAR)) {
      filePath = filePath.substring( 1, filePath.length() );
    }
    return filePath.substring( 0, filePath.lastIndexOf( CommonParams.PATH_SEPARATOR_CHAR ) + 1 );
  }

  public String getParentDirectoryPathFromFilePath(String filePath) {
    Path directoryPath = Paths.get(filePath).getParent();
    if (directoryPath != null) {
      return directoryPath.toString();
    } else {
      return null;
    }
  }

  public String findFileAndGetFilePath ( String fileNameWildcardToFind, String directoryPathOnFind ) {
    File directory = new File( directoryPathOnFind );

    FileFilter fileFilter = new WildcardFileFilter( fileNameWildcardToFind );
    File[] files = directory.listFiles( fileFilter );

    if ( files.length != 0 ) {
      return files[0].getAbsolutePath();
    } else {
      return null;
    }
  }

  /**
   * Función para contar cuantos archivos hay en una carpeta. Si la carpeta no existe, devolverá cero y podrá este valor
   * ayudar a tomar decisiones sobre la creación.
   * @param rutaCarpeta (<code>String</code>) Cadena con la carpeta a ser explorada.
   * @return (<code>int</code>) cantidad de archivos en la carpeta especificada. Devolverá 999999999 en caso de que se
   * haya pasado una carpeta inexistente.
   * @see #contarArchivosEnCarpeta ( String rutaCarpeta, boolean esRecursivo )
   */
  public int contarArchivosEnCarpeta ( String rutaCarpeta ) {
    return contarArchivosEnCarpeta( rutaCarpeta, false );
  }

  /**
   * Función para contar cuantos archivos hay en una carpeta. Si la carpeta no existe, devolverá cero y podrá este valor
   * ayudar a tomar decisiones sobre la creación.
   * @param rutaCarpeta (<code>String</code>) Cadena con la carpeta a ser explorada.
   * @param esRecursivo (<code>boolean</code>) indica si se desea exploración recursiva (dentro de otras carpetas).
   * @return (<code>int</code>) cantidad de archivos en la carpeta especificada.
   */
  public int contarArchivosEnCarpeta ( String rutaCarpeta, boolean esRecursivo ) {
    File f = new File( rutaCarpeta );
    int cuenta = 0;

    File[] files = f.listFiles();

    if ( files != null ) {
      for ( int i = 0; i < files.length; i++ ) {
        cuenta++;
        File file = files[i];

        if ( file.isDirectory() && esRecursivo ) {
          contarArchivosEnCarpeta( file.getAbsolutePath(), esRecursivo );
        }
      }
    }

    return cuenta;
  }

  public void openFile(String filePath) {
    if (existsFile(filePath)) {
      try {
        Desktop.getDesktop().open(new File(filePath));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } 
  }
}
