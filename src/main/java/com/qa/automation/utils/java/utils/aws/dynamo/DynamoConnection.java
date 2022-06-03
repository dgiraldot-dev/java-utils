package com.qa.automation.utils.java.utils.aws.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.qa.automation.utils.java.utils.aws.authentication.AwsAuthentication;

public class DynamoConnection {

  private AwsAuthentication awsAuthentication = new AwsAuthentication();
  private AmazonDynamoDB amazonDynamoDB;
  private DynamoDB dynamoDB;
  private DynamoDBMapper dynamoDBMapper;

  public DynamoConnection() {
    amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(awsAuthentication.getAwsStaticCredentialsProvider()).build();
    dynamoDB = new DynamoDB(amazonDynamoDB);
    dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
  }

  public DynamoDB getDynamoDB() {
    return dynamoDB;
  }
  
  public DynamoDBMapper getDynamoDBMapper() {
    return dynamoDBMapper;
  }
}