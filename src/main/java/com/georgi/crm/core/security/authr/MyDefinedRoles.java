package com.georgi.crm.core.security.authr;

public class MyDefinedRoles {
  
  public static final String ADMIN = "ROLE_ADMIN";
  public static final String CUSTOMER = "ROLE_CUSTOMER";
  public static final String ESCAL_MGR = "ROLE_ESCALATION_MANAGER";
  public static final String PROCESSOR = "ROLE_PROCESSOR";
  
  public static final MyAuthority ROLE_ADMIN = new MyAuthority(ADMIN, true);
  public static final MyAuthority ROLE_PROCESSOR = new MyAuthority(CUSTOMER, true);
  public static final MyAuthority ROLE_ESCAL_MGR = new MyAuthority(ESCAL_MGR, true);
  public static final MyAuthority ROLE_CUSTOMER = new MyAuthority(PROCESSOR, true);
  
}
