package com.georgi.crm.core.ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<TicketImpl, Long>, JpaSpecificationExecutor<TicketImpl> {
  List<TicketImpl> findByCreatorUsername(String username);
  List<TicketImpl> findByProcessorUsername(String username);
}