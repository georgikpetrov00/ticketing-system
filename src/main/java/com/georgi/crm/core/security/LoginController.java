package com.georgi.crm.core.security;

import com.georgi.crm.core.user.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class LoginController {
  
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserDetailsService userDetailsService;
  
  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
    try {
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password)
      );
      
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = tokenProvider.generateJwtToken(authentication);
      return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }
  }
  
  @GetMapping("/login")
  public String login() {
    return "login";
  }
}
