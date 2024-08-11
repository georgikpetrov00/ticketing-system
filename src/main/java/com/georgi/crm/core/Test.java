package com.georgi.crm.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

public class Test {
  
  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put("myKey", "mYValue");
    properties.put("MyKey2", new ArrayList<>());
    
    System.out.println(properties);
    
    System.out.println(properties.get("MyKey2"));
  }
  
}
