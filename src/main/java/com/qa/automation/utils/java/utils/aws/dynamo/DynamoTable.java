package com.qa.automation.utils.java.utils.aws.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.Iterator;

public class DynamoTable {

  private DynamoDB dynamoDB = new DynamoConnection().getDynamoDB();

  private Table table;
  private Item item;
  private PutItemOutcome putItemOutcome;

  public DynamoTable(String tableName) {
    table = dynamoDB.getTable(tableName);
  }

  public Table getTable() {
    return table;
  }
  
  public Item getItem() {
    return item;
  }
  
  public PutItemOutcome getPutItemOutcome() {
    return putItemOutcome;
  }
  
  public Item getItemByKey(String hashKeyName, String hashKeyValue) {
    this.item = table.getItem(hashKeyName, hashKeyValue);
    return this.item;
  }
  
  public Item getItemByKey(String hashKeyName, String hashKeyValue, String rangeKeyName, String rangeKeyValue) {
    this.item = table.getItem(new PrimaryKey( hashKeyName, hashKeyValue, rangeKeyName, rangeKeyValue));
    return this.item;
  }
  
  public PutItemOutcome putItem(Item item) {
    this.putItemOutcome = table.putItem(item);
    return this.putItemOutcome;
  }
  
  public PutItemOutcome putItem() {
    return table.putItem(this.item);
  }

  public JsonObject getJsonItemByKey(String hashKeyName, String hashKeyValue) {    
    return JsonParser.parseString(getItemByKey(hashKeyName, hashKeyValue).toJSON()).getAsJsonObject();
  }
  
  public JsonObject getJsonItemByKey(String hashKeyName, String hashKeyValue, String rangeKeyName, String rangeKeyValue) {    
    return JsonParser.parseString(getItemByKey(hashKeyName, hashKeyValue, rangeKeyName, rangeKeyValue).toJSON()).getAsJsonObject();
  }

  public String getStringItemByKey(String hashKeyName, String hashKeyValue, String valueSeparator, boolean sortValues) {    
    String columnValues = null;
    String columnValue;
    String recordValues = null;
    String recordValue;

    JsonObject jsonObject = getJsonItemByKey(hashKeyName, hashKeyValue) ;
    Object[] keys = jsonObject.keySet().toArray();

    if (sortValues) Arrays.sort(keys);

    for (Object object : keys) {
      columnValue = object.toString();

      if (!jsonObject.get(object.toString()).isJsonArray()) {
        recordValue = jsonObject.get(object.toString()).getAsString(); 
      } else {
        recordValue = jsonObject.get(object.toString()).getAsJsonArray().toString();
      }

      if (columnValues == null) {
        columnValues = columnValue; 
        recordValues = recordValue;
      } else {
        columnValues = new StringBuilder(columnValues).append(valueSeparator).append(columnValue).toString(); 
        recordValues = new StringBuilder(recordValues).append(valueSeparator).append(recordValue).toString();
      }
    }

    return new StringBuilder(columnValues).append("\n").append(recordValues).toString();
  }

  public String getStringItemByKey(String hashKeyName, String hashKeyValue, String valueSeparator) {
    return getStringItemByKey(hashKeyName, hashKeyValue, valueSeparator, false);
  }

  public void deleteAllItems(String hashKeyName, String rangeKeyName) {
    ScanSpec scanSpec = new ScanSpec();

    ItemCollection<ScanOutcome> items = table.scan(scanSpec);
    Iterator<Item> iterator = items.iterator();

    while (iterator.hasNext()) {
      Item itemToDelete = iterator.next();
      String hashKey = itemToDelete.getString(hashKeyName);
      String rangeKey = itemToDelete.getString(rangeKeyName);
      PrimaryKey key = new PrimaryKey( hashKeyName, hashKey, rangeKeyName, rangeKey);
      table.deleteItem(key);
    }
  }
  
  public void deleteAllItems(String hashKeyName) {
    ScanSpec scanSpec = new ScanSpec();

    ItemCollection<ScanOutcome> items = table.scan(scanSpec);
    Iterator<Item> iterator = items.iterator();

    while (iterator.hasNext()) {
      Item itemToDelete = iterator.next();
      String hashKey = itemToDelete.getString(hashKeyName);
      PrimaryKey key = new PrimaryKey(hashKeyName, hashKey);
      table.deleteItem(key);
    }
  }
  
  public int countItems() {
    int numberOfItems = 0;

    ScanSpec scanSpec = new ScanSpec();

    ItemCollection<ScanOutcome> items = table.scan(scanSpec);
    Iterator<Item> iterator = items.iterator();

    while (iterator.hasNext()) {
      iterator.next();
      numberOfItems = numberOfItems + 1;
    }
    
    return numberOfItems;
  }
}