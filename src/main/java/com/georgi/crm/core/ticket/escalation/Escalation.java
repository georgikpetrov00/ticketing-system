package com.georgi.crm.core.ticket.escalation;

import java.time.LocalDateTime;

import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
public class Escalation {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private boolean isApproved;
  
  @OneToOne(cascade = CascadeType.ALL)
  private TicketImpl ticket;
  
  private LocalDateTime requestTime;
  
  @ManyToOne
  @JoinColumn(name = "escalation_manager_id")
  private UserDetailsImpl escalationManager;
  
  public Escalation() {
  
  }
  
}
