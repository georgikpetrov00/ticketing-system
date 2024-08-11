package com.georgi.crm.core.ticket.message;

import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketService;
import com.georgi.crm.core.ticket.message.file.FileStorageService;
import com.georgi.crm.core.ticket.util.Constants;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TicketMessageService {
  
  private final TicketMessageRepository ticketMessageRepository;
  private final TicketService ticketService;
  
  public TicketMessage save(TicketMessage message) {
    return ticketMessageRepository.save(message);
  }
  
  @Transactional
  public TicketMessage logicalSave(TicketMessage message, TicketImpl ticket) {
    message.setTicket(ticket);
    message = save(message);
    
    ticketService.addTicketMessage(ticket, message);
    
    return message;
  }
  
  public TicketMessage findById(Long id) {
    return ticketMessageRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Message not found"));
  }
  
  public List<TicketMessage> findAll() {
    return ticketMessageRepository.findAll();
  }
  
  public List<TicketMessage> findMessagesBySenderId(Long senderId) {
    return ticketMessageRepository.findBySenderId(senderId);
  }
  
  public void delete(Long id) {
    ticketMessageRepository.deleteById(id);
  }
}
