package com.georgi.crm.core.ticket.details;

import com.georgi.crm.core.ticket.details.util.ProcurementCurrency;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@DiscriminatorValue("PROCUREMENT")
public class ProcurementTicketDetails extends TicketDetails {

  private String itemToPurchase;
  
  private double amount;
  
  @Enumerated(EnumType.STRING)
  private ProcurementCurrency currency;
  
}
