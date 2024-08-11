package com.georgi.crm.core.security;

import java.util.Optional;

import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class is used for fetching the users from the DB during authentication
 */

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  
  private final UserDetailsRepository userDetailsRepository;
  
  @Override
  public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserDetailsImpl> user = userDetailsRepository.findByUsername(username);
    
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User " + username + " not found");
    }
    
    return user.get();
  }
  
  public static UserDetailsImpl getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      
      if (principal instanceof UserDetailsImpl) {
        return ((UserDetailsImpl) principal);
      }
    }
    
    return null;
  }
}