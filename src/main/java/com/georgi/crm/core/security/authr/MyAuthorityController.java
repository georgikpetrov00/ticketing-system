package com.georgi.crm.core.security.authr;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authority")
public class MyAuthorityController {
  
  private final MyAuthorityService myAuthorityService;
  
  @Autowired
  public MyAuthorityController(MyAuthorityService myAuthorityService) {
    this.myAuthorityService = myAuthorityService;
  }
  
  @GetMapping
  public ResponseEntity<List<MyAuthority>> getAllAuthorities() {
    List<MyAuthority> authorities = myAuthorityService.getAllAuthorities();
    return ResponseEntity.ok(authorities);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<MyAuthority> getAuthorityById(@PathVariable Long id) {
    Optional<MyAuthority> authority = myAuthorityService.getAuthorityById(id);
    return authority.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
  
  @GetMapping("/name/{name}")
  public ResponseEntity<MyAuthority> getAuthorityByName(@PathVariable String name) {
    Optional<MyAuthority> authority = myAuthorityService.getAuthorityByName(name);
    return authority.map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
  
  @PostMapping
  public ResponseEntity<MyAuthority> createAuthority(@RequestBody MyAuthority authority) {
    MyAuthority savedAuthority = myAuthorityService.saveAuthority(authority);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthority);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthorityById(@PathVariable Long id) {
    myAuthorityService.deleteAuthorityById(id);
    return ResponseEntity.noContent().build();
  }
}