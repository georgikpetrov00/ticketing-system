package com.georgi.crm.core.ticket.details;

import com.georgi.crm.core.ticket.details.util.EquipmentType;
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
@DiscriminatorValue("EQUIPMENT")
public class EquipmentTicketDetails extends TicketDetails {
  
  @Enumerated(EnumType.STRING)
  private EquipmentType equipmentType;
  
}
