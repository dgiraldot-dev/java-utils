package com.qa.automation.utils.java.utils.aws.authentication;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.jayway.jsonpath.DocumentContext;
import com.qa.automation.utils.java.utils.common.FileOprs;
import com.qa.automation.utils.java.utils.exception.GenericRuntimeException;
import com.qa.automation.utils.java.utils.json.JsonOprs;

public class AwsAuthentication {

  private static final String DEFAULT_AWS_CREDENTIALS_FILE_NAME = "AwsCredentials.json";
  private String awsCredentialsFilePath;

  private String awsAccessKey;
  private String awsSecretKey;
  private String sessionToken;
  
  private BasicSessionCredentials basicSessionCredentials;
  private AWSStaticCredentialsProvider awsStaticCredentialsProvider;
  
  public AwsAuthentication(String awsCredentialsFilePath) {
    this.awsCredentialsFilePath = awsCredentialsFilePath;
    getCredentialValues();
    startConnection();
  }
  
  public AwsAuthentication() {
    this.awsCredentialsFilePath = new FileOprs().tryFindFileAndReturnPath(DEFAULT_AWS_CREDENTIALS_FILE_NAME);
    getCredentialValues();
    startConnection();
  }
  
  private void getCredentialValues() {
    if (awsCredentialsFilePath == null) throw new GenericRuntimeException("The AWS credentials file was not found");

    DocumentContext documentContext = new JsonOprs().getDocumentContext(awsCredentialsFilePath);
    awsAccessKey = documentContext.read("$.awsAccessKey");
    awsSecretKey = documentContext.read("$.awsSecretKey");
    sessionToken = documentContext.read("$.sessionToken");
  }
  
  private void startConnection() {
    basicSessionCredentials = new BasicSessionCredentials(awsAccessKey, awsSecretKey, sessionToken);
    awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicSessionCredentials);
  }  

  public String getAwsAccessKey() {
    return awsAccessKey;
  }

  public String getAwsSecretKey() {
    return awsSecretKey;
  }

  public String getSessionToken() {
    return sessionToken;
  }
  
  public BasicSessionCredentials getBasicSessionCredentials() {
    return basicSessionCredentials;
  }
  
  public AWSStaticCredentialsProvider getAwsStaticCredentialsProvider() {
    return awsStaticCredentialsProvider;
  }
}