package com.georgi.crm.core.ticket.message;

import java.time.LocalDateTime;
import java.util.List;

import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.Ticket;
import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketService;
import com.georgi.crm.core.ticket.message.impl.text.TextMessage;
import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/message")
public class TicketMessageController {
  
  private final TicketService ticketService;
  private final TicketMessageService ticketMessageService;
  private final UserDetailsService userDetailsService;
  
  @PostMapping("/create")
  public ResponseEntity<TicketMessageDTO> createMessage(@RequestBody TicketMessageDTO messageDTO) {
    TextMessage message = new TextMessage();
    UserDetailsImpl sender = userDetailsService.getUserByUsername(UserDetailsServiceImpl.getCurrentUser().getUsername());
    TicketImpl ticket = ticketService.getTicketById(messageDTO.getTicketId());
    ticket.setLastChangeTime(LocalDateTime.now());
    
    message.setSender(sender);
    message.setTicket(ticket);
    message.setTimestamp(LocalDateTime.now());
    message.setTextContent(messageDTO.getContent());
    
    TicketMessage createdMessage = ticketMessageService.save(message);
    return new ResponseEntity<>(new TicketMessageDTO(createdMessage), HttpStatus.CREATED);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<TicketMessage> getMessage(@PathVariable Long id) {
    TicketMessage message = ticketMessageService.findById(id);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
  
  @GetMapping
  public ResponseEntity<List<TicketMessage>> getAllMessages() {
    List<TicketMessage> messages = ticketMessageService.findAll();
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
    ticketMessageService.delete(id);
    return ResponseEntity.noContent().build();
  }
}