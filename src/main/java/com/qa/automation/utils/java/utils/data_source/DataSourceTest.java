package com.qa.automation.utils.java.utils.data_source;

public class DataSourceTest {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource("src/test/resources/data_sources/DataSourceTemplate.xlsx", "DataSource01");

        System.out.println(dataSource.getFullKeyList());
        System.out.println(dataSource.getFilteredDataValues());
    }
}
