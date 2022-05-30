/**
 * Grupo Aval Acciones y Valores S.A. CONFIDENTIAL
 *
 * <p>Copyright (c) 2018 . All Rights Reserved.
 *
 * <p>NOTICE: This file is subject to the terms and conditions defined in file 'LICENSE', which is
 * part of this source code package.
 */
package com.qa.automation.utils.java.utils.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class S3Bucket {

  private static final String SLASH = "/";
  private static final String BACK_SLASH = "\\";

  private AmazonS3 amazonS3 = new S3Connection().getAmazonS3();

  private String fullPath;
  private String bucketName;
  private String prefix;

  public S3Bucket(String bucketFullPath) {
    setBucketNameParts(bucketFullPath);
  }

  public String getFullPath() {
    return fullPath;
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getPrefix() {
    return prefix;
  }

  public void sendFile(String sourceFilePath, String filenameToSetInBucket, String contentType) {
    File file = new File(sourceFilePath);
    if (filenameToSetInBucket == null) filenameToSetInBucket = file.getName();

    PutObjectRequest putObjectRequest = new PutObjectRequest(fullPath, filenameToSetInBucket, file);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(contentType);
    putObjectRequest.setMetadata(objectMetadata);
    amazonS3.putObject(putObjectRequest);
  }

  public void sendFile(String sourceFilePath, String contentType) {
    sendFile(sourceFilePath, null, contentType);
  }

  public void sendPlainTextFile(String sourceFilePath, String filenameToSetInBucket) {
    sendFile(sourceFilePath, filenameToSetInBucket, "plain/text");
  }

  public void sendPlainTextFile(String sourceFilePath) {
    sendPlainTextFile(sourceFilePath, null);
  }

  public void sendJsonFile(String sourceFilePath, String filenameToSetInBucket) {
    sendFile(sourceFilePath, filenameToSetInBucket, "application/json");
  }

  public void sendJsonFile(String sourceFilePath) {
    sendJsonFile(sourceFilePath, null);
  }

  private void setBucketNameParts(String bucketFullPath) {
    int lengthPath = bucketFullPath.length();
    String firstChar = bucketFullPath.substring(0, 1);
    String lastChar = bucketFullPath.substring(lengthPath-1, lengthPath);

    if (lastChar.equals(SLASH) || lastChar.equals(BACK_SLASH)) {
      bucketFullPath = bucketFullPath.substring(0, lengthPath-1);
    }

    if (firstChar.equals(SLASH) || firstChar.equals(BACK_SLASH)) {
      bucketFullPath = bucketFullPath.substring(1);
    }

    int indexOfFilePathSeparator = bucketFullPath.indexOf(SLASH);

    if (indexOfFilePathSeparator == -1) indexOfFilePathSeparator = bucketFullPath.indexOf(BACK_SLASH);

    this.fullPath = bucketFullPath;
    this.bucketName = bucketFullPath.substring(0, indexOfFilePathSeparator);
    this.prefix = bucketFullPath.substring(indexOfFilePathSeparator+1, bucketFullPath.length());
  }
}