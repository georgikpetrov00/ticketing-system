package com.georgi.crm.core.ticket.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
  
  List<TicketMessage> findBySenderId(Long senderId);
  
}