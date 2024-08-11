package com.georgi.crm.core.ticket.escalation;

import java.time.LocalDateTime;
import java.util.List;

import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketService;
import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/escalation")
public class EscalationController {
  
  private EscalationService escalationService;
  private TicketService ticketService;
  private UserDetailsService userService;
  
  // Създаване на нова ескалация
  @PostMapping("/create")
  public ResponseEntity<Escalation> createEscalation(@RequestParam Long ticketId) {
    TicketImpl ticket = ticketService.getTicketById(ticketId);
    
    if (ticket == null) {
      return ResponseEntity.notFound().build();
    }
    
    Escalation escalation = new Escalation();
    escalation.setTicket(ticket);
    escalation.setRequestTime(LocalDateTime.now());
    escalation.setApproved(false);
    
    Escalation createdEscalation = escalationService.saveEscalation(escalation);
    return new ResponseEntity<>(createdEscalation, HttpStatus.CREATED);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Escalation> getEscalationById(@PathVariable Long id) {
    Escalation escalation = escalationService.getEscalationById(id);
    if (escalation == null) {
      return ResponseEntity.notFound().build();
    }
    return new ResponseEntity<>(escalation, HttpStatus.OK);
  }
  
  @GetMapping("/all")
  public ResponseEntity<List<Escalation>> getAllEscalations() {
    List<Escalation> escalations = escalationService.getAllEscalations();
    return new ResponseEntity<>(escalations, HttpStatus.OK);
  }
  
  // FIXME make the security to require ESCAL_MGR role || escal mgr authority
  @PutMapping("/approve/{id}")
  public ResponseEntity<Escalation> approveEscalation(@PathVariable Long id, @RequestParam boolean approve) {
    Escalation escalation = escalationService.getEscalationById(id);
    if (escalation == null) {
      return ResponseEntity.notFound().build();
    }
    escalation.setApproved(approve);
    Escalation updatedEscalation = escalationService.saveEscalation(escalation);
    return new ResponseEntity<>(updatedEscalation, HttpStatus.OK);
  }
  
  // FIXME make the security to require ESCAL_MGR role || escal mgr authority
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteEscalation(@PathVariable Long id) {
    Escalation escalation = escalationService.getEscalationById(id);
    if (escalation == null) {
      return ResponseEntity.notFound().build();
    }
    escalationService.deleteEscalation(id);
    return ResponseEntity.noContent().build();
  }
}