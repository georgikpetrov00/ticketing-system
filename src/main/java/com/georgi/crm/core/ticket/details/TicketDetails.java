package com.georgi.crm.core.ticket.details;

import com.georgi.crm.core.ticket.TicketImpl;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ticket_details_type")
public abstract class TicketDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;
  
  @OneToOne(cascade = CascadeType.ALL)
  protected TicketImpl ticket;
  
}
