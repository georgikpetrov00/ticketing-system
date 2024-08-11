package com.georgi.crm.core.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.georgi.crm.core.ticket.util.TicketComponent;
import lombok.Getter;

@Getter
public class UserDetailsDto {
  
  public String username;
  public String email;
  public String secondEmail;
  public String phoneNumber;
  public String country;
  public String firstName;
  public String lastName;
  public Map<String, String> userAttributes;
  public List<String> componentsResponsible;
  
  public UserDetailsDto(UserDetailsImpl userDetailsImpl) {
    this.username = userDetailsImpl.getUsername();
    this.email = userDetailsImpl.getEmail();
    this.secondEmail = userDetailsImpl.getSecondEmail();
    this.phoneNumber = userDetailsImpl.getPhoneNumber();
    this.country = userDetailsImpl.getCountry();
    this.firstName = userDetailsImpl.getFirstName();
    this.lastName = userDetailsImpl.getLastName();
    this.userAttributes = new HashMap<>();
    for (UserAttribute userAttribute : userDetailsImpl.getUserAttributes()) {
      userAttributes.put(userAttribute.getAttrKey(), userAttribute.getAttrValue());
    }
    this.componentsResponsible = new ArrayList<>();
    for (TicketComponent component : userDetailsImpl.getComponentsResponsible()) {
      componentsResponsible.add(component.toString());
    }
  }
  
}
