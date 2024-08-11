package com.georgi.crm.core.ticket.message;

import java.time.LocalDateTime;

import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "message_type")
public abstract class TicketMessage {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_id")
  protected TicketImpl ticket;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  protected UserDetailsImpl sender;
  
  protected LocalDateTime timestamp;

  public boolean getAvailableForUpdate() {
    return LocalDateTime.now().isBefore(timestamp.plusMinutes(15));
  }
  
}