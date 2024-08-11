package com.georgi.crm.core.security.authr;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@Data
@Entity
@Table(name = "authority")
public class MyAuthority implements GrantedAuthority {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String authorityName;
  
  private boolean isRole;
  
  public MyAuthority(String authorityName) {
    this.authorityName = authorityName;
    this.isRole = false;
  }
  
  public MyAuthority(String authorityName, boolean isRole) {
    this.authorityName = authorityName;
    this.isRole = isRole;
  }
  
  @Override
  public String getAuthority() {
    return authorityName;
  }
}
