package com.georgi.crm.core.ticket.details;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("FACILITY")
public class FacilityTicketDetails extends TicketDetails {

  private String building;
  private String info;
  private int floor;
  private int room;

}
