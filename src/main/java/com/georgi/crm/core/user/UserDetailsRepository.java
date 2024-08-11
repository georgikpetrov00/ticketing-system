package com.georgi.crm.core.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsImpl, Long> {
  
  Optional<UserDetailsImpl> findByEmail(String email);
  
  Optional<UserDetailsImpl> findByUsername(String username);
}