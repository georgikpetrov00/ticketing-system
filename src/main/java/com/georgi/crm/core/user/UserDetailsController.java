package com.georgi.crm.core.user;

import java.util.List;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserDetailsController {
  private final UserDetailsService userDetailsService;
  
  @GetMapping
  public List<UserDetailsImpl> getAllUsers() {
    return userDetailsService.getAllUsers();
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<UserDetailsImpl> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userDetailsService.getUserById(id));
    
//    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
  
  @GetMapping("/username/{username}")
  public ResponseEntity<UserDetailsImpl> getUserByUsername(@PathVariable String username) {
    UserDetailsImpl user = userDetailsService.getUserByUsername(username);
    return ResponseEntity.ok(user);
  }
  
  @PostMapping
  public UserDetailsImpl createUser(@RequestBody UserDetailsImpl user) {
    return userDetailsService.createUser(user);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<UserDetailsImpl> updateUser(@PathVariable Long id, @RequestBody UserDetailsImpl userDetails) {
    try {
      UserDetailsImpl updatedUser = userDetailsService.updateUser(id, userDetails);
      return ResponseEntity.ok(updatedUser);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userDetailsService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
  
//  @GetMapping("/info")
//  public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Principal principal) {
//    UserDetailsImpl user = userDetailsService.getUserByUsername(principal.getName());
//
//    Map<String, Object> response = new HashMap<>();
//    response.put("username", user.getUsername());
//
//
//    return ResponseEntity.ok(response);
//  }
@GetMapping("/info")
public ResponseEntity<UserDetailsDto> getUserInfo() {
  UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  user = userDetailsService.getUserByUsername(user.getUsername());
  return new ResponseEntity<>(new UserDetailsDto(user), HttpStatus.OK);
}
}