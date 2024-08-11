package com.georgi.crm.core;

import java.security.Principal;

import com.georgi.crm.core.security.SecurityConfig;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.user.UserAttribute;
import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/")
@AllArgsConstructor
public class LandingPage {
  
  public final UserDetailsService userDetailsService;
  public final SecurityConfig securityConfig;
  
  @GetMapping("/")
  public String hello(Principal principal) {
    UserDetailsImpl user = userDetailsService.getUserByUsername(principal.getName());
    
    String lineSeparator = "<br>";
    StringBuilder sb = new StringBuilder();
    
    for (UserAttribute attribute : user.getUserAttributes()) {
      sb.append("&nbsp;").append(attribute.getAttrKey()).append(":").append(attribute.getAttrValue()).append(lineSeparator);
    }
    
    for (TicketMessage message : user.getMessages()) {
      sb.append("&nbsp;").append(message.toString()).append(lineSeparator);
    }
    
    return "Hello, " + user.getUsername()
      + lineSeparator
      + "User Attributes:"
      + lineSeparator
      + sb
      + lineSeparator + lineSeparator + lineSeparator + lineSeparator
      + user;
  }
  
  @GetMapping("/admin")
  public String helloAdmin(Principal principal) {
    return hello(principal);
  }
  
  @GetMapping("/processor")
  public String helloProcessor(Principal principal) {
    return hello(principal);
  }
  
  @GetMapping("/customer")
  public String helloCustomer(Principal principal) {
    return hello(principal);
  }
  
}
