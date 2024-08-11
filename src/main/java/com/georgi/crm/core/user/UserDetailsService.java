package com.georgi.crm.core.user;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsService {
  
  private final UserDetailsRepository userDetailsRepository;
  
  public List<UserDetailsImpl> getAllUsers() {
    return userDetailsRepository.findAll();
  }
  
  public UserDetailsImpl getUserById(Long id) {
    return userDetailsRepository.findById(id).get();
  }
  
  public UserDetailsImpl getUserByUsername(String username) {
    return userDetailsRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User " + username + " does not exist."));
  }
  
  public UserDetailsImpl createUser(UserDetailsImpl user) {
    return userDetailsRepository.save(user);
  }
  
  public UserDetailsImpl updateUser(Long id, UserDetailsImpl userDetails) {
    Optional<UserDetailsImpl> existingUser = userDetailsRepository.findById(id);
    if (existingUser.isPresent()) {
      UserDetailsImpl user = existingUser.get();
      user.setUsername(userDetails.getUsername());
      user.setPassword(userDetails.getPassword());
      user.setEmail(userDetails.getEmail());
      user.setAuthorities(userDetails.getAuthoritiez());
      user.setUserAttributes(userDetails.getUserAttributes());
      return userDetailsRepository.save(user);
    } else {
      throw new RuntimeException("User not found");
    }
  }
  
  public void deleteUser(Long id) {
    userDetailsRepository.deleteById(id);
  }
}
