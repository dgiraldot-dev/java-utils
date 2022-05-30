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
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.qa.automation.utils.java.utils.aws.authentication.AwsAuthentication;

public class S3Connection {

  private AwsAuthentication awsAuthentication = new AwsAuthentication();
  private AmazonS3 amazonS3;

  public S3Connection() {
    amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(awsAuthentication.getAwsStaticCredentialsProvider()).build();
  }

  public AmazonS3 getAmazonS3() {
    return amazonS3;
  }
}