package com.qa.automation.utils.java.utils.aws.dynamo;

public class DynamoTableTest {
    public static void main(String[] args) {
        DynamoTable dynamoTable = new DynamoTable("bb-ent-stg-guarantee");
        System.out.println(dynamoTable.countItems());
    }
}