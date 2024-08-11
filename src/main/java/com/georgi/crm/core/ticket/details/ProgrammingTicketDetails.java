package com.georgi.crm.core.ticket.details;

import com.georgi.crm.core.ticket.details.util.OperatingSystemType;
import com.georgi.crm.core.ticket.details.util.ProgrammingLanguage;
import com.georgi.crm.core.ticket.details.util.SystemType;
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
@DiscriminatorValue("PROGRAMMING")
public class ProgrammingTicketDetails extends TicketDetails {
  
  @Enumerated(EnumType.STRING)
  private OperatingSystemType osType;
  
  @Enumerated(EnumType.STRING)
  private ProgrammingLanguage programmingLanguage;
  
  @Enumerated(EnumType.STRING)
  private SystemType systemType;
  
}
