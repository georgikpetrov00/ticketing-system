package com.georgi.crm.core.security.authr;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyAuthorityService {
  
  private final MyAuthorityRepository myAuthorityRepository;
  
  public List<MyAuthority> getAllAuthorities() {
    return myAuthorityRepository.findAll();
  }
  
  public MyAuthority saveAuthority(MyAuthority authority) {
    return myAuthorityRepository.save(authority);
  }
  
  public void deleteAuthorityById(Long id) {
    myAuthorityRepository.deleteById(id);
  }
  
  public Optional<MyAuthority> getAuthorityById(Long id) {
    return myAuthorityRepository.findById(id);
  }
  
  public Optional<MyAuthority> getAuthorityByName(String authorityName) {
    return myAuthorityRepository.findByAuthorityName(authorityName);
  }
}