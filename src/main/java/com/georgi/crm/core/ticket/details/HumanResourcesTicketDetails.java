package com.georgi.crm.core.ticket.details;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("HUMAN_RESOURCES")
public class HumanResourcesTicketDetails extends TicketDetails {

  private String details;
  
}
