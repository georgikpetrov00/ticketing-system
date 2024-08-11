package com.georgi.crm.core.security.authr;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAuthorityRepository extends JpaRepository<MyAuthority, Long> {
  
  Optional<MyAuthority> findByAuthorityName(String authorityName);
  
}