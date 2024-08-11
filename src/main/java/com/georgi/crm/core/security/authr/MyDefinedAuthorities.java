package com.georgi.crm.core.security.authr;

public class MyDefinedAuthorities {
  
  private MyDefinedAuthorities() {

  }
  
  public static final String ADMIN = "ADMIN";
  public static final String CUSTOMER = "CUSTOMER";
  public static final String ESCAL_MGR = "ESCALATION_MANAGER";
  public static final String PROCESSOR = "PROCESSOR";
  
  public static final MyAuthority AUTHORITY_ADMIN = new MyAuthority(ADMIN);
  public static final MyAuthority AUTHORITY_CUSTOMER = new MyAuthority(CUSTOMER);
  
  public static final MyAuthority AUTHORITY_ESCAL_MGR = new MyAuthority(ESCAL_MGR);
  public static final MyAuthority AUTHORITY_PROCESSOR = new MyAuthority(PROCESSOR);
  
}
