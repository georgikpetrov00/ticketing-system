package com.georgi.crm.core.ticket.message.impl.text;

import com.georgi.crm.core.SessionUtil;
import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketService;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.message.TicketMessageService;
import com.georgi.crm.core.ticket.message.impl.file.FileMessage;
import com.georgi.crm.core.ticket.message.impl.image.ImageMessage;
import com.georgi.crm.core.ticket.message.impl.image.ImageMessageDTO;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/textMessage")
public class TextMessageController {
  
  private final TicketMessageService ticketMessageService;
  private final TicketService ticketService;
  private final UserDetailsServiceImpl userDetailsService;
  
  @PostMapping("/create")
  public ResponseEntity<TextMessage> logicalCreateTextMessage(@RequestBody TextMessageDTO messageDTO, @RequestParam Long ticketId) {
    UserDetailsImpl currentUser = userDetailsService.loadUserByUsername(UserDetailsServiceImpl.getCurrentUser().getUsername());
    TicketImpl ticket = ticketService.getTicketById(ticketId);
    TextMessage imageMessage = new TextMessage(null, ticket, currentUser, messageDTO.getTextContent());
    
    return new ResponseEntity<>((TextMessage) ticketMessageService.logicalSave(imageMessage, ticket), HttpStatus.CREATED);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<TextMessage> getTextMessage(@PathVariable Long id) {
    TextMessage message = (TextMessage) ticketMessageService.findById(id);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
  
  @GetMapping
  public ResponseEntity<List<TicketMessage>> getAllTextMessages() {
    List<TicketMessage> messages = ticketMessageService.findAll();
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTextMessage(@PathVariable Long id) {
    ticketMessageService.delete(id);
    return ResponseEntity.noContent().build();
  }
}