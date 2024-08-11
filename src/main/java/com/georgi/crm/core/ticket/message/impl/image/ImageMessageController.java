package com.georgi.crm.core.ticket.message.impl.image;

import java.util.List;
import java.util.stream.Collectors;

import com.georgi.crm.core.SessionUtil;
import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketService;
import com.georgi.crm.core.ticket.message.TicketMessageService;
import com.georgi.crm.core.ticket.message.impl.file.FileMessage;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/imageMessage")
public class ImageMessageController {
  
  private final TicketMessageService ticketMessageService;
  private final TicketService ticketService;
  
  @PostMapping
  public ResponseEntity<ImageMessage> logicalCreateImageMessage(@RequestBody ImageMessageDTO messageDTO, HttpSession httpSession) {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    Long ticketId = SessionUtil.getCurrentTicketIdFromSession(httpSession);
    TicketImpl ticket = ticketService.getTicketById(ticketId);
    ImageMessage imageMessage = new ImageMessage(null, ticket, currentUser, messageDTO.getImageFileName());
    
    return new ResponseEntity<>((ImageMessage) ticketMessageService.logicalSave(imageMessage, ticket), HttpStatus.CREATED);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<ImageMessage> getImageMessage(@PathVariable Long id) {
    ImageMessage message = (ImageMessage) ticketMessageService.findById(id);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
  
  @GetMapping
  public ResponseEntity<List<ImageMessage>> getAllImageMessages() {
    List<ImageMessage> messages = ticketMessageService.findAll().stream()
      .filter(msg -> msg instanceof ImageMessage)
      .map(msg -> (ImageMessage) msg)
      .collect(Collectors.toList());
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteImageMessage(@PathVariable Long id) {
    ticketMessageService.delete(id);
    return ResponseEntity.noContent().build();
  }
}