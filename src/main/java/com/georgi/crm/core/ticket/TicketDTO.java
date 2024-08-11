package com.georgi.crm.core.ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.georgi.crm.core.ticket.details.EquipmentTicketDetails;
import com.georgi.crm.core.ticket.details.FacilityTicketDetails;
import com.georgi.crm.core.ticket.details.HumanResourcesTicketDetails;
import com.georgi.crm.core.ticket.details.NoCategoryTicketDetails;
import com.georgi.crm.core.ticket.details.ProcurementTicketDetails;
import com.georgi.crm.core.ticket.details.ProgrammingTicketDetails;
import com.georgi.crm.core.ticket.details.TicketDetails;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.message.TicketMessageDTO;
import com.georgi.crm.core.ticket.util.TicketComponent;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.ticket.util.TicketStatus;
import com.georgi.crm.core.user.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {
  
  private Long id;
  private String description;
  private String currentComponent;
  private String priority;
  private String priorityEn;
  private String priorityBg;
  private String lastChangeTime;
  private String creatorFirstName;
  private String processorFirstName;
  private String stateEn;
  private String stateBg;
  private List<TicketMessageDTO> messages;
  
  @JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
  )
  @JsonSubTypes({
    @JsonSubTypes.Type(value = EquipmentTicketDetails.class, name = "EQUIPMENT"),
    @JsonSubTypes.Type(value = FacilityTicketDetails.class, name = "FACILITY"),
    @JsonSubTypes.Type(value = HumanResourcesTicketDetails.class, name = "HUMAN_RESOURCES"),
    @JsonSubTypes.Type(value = NoCategoryTicketDetails.class, name = "NO_CATEGORY"),
    @JsonSubTypes.Type(value = ProgrammingTicketDetails.class, name = "PROGRAMMING"),
    @JsonSubTypes.Type(value = ProcurementTicketDetails.class, name = "PROCUREMENT")
  })
  private TicketDetails details;
  
  public TicketDTO(TicketImpl ticket) {
    this.id = ticket.getId();
    this.description = ticket.getDescription();
    this.currentComponent = ticket.getCurrentComponent().toString();
    this.priority = ticket.getPriority().toString();
    this.priorityEn = ticket.getPriority().getEnglishText();
    this.priorityBg = ticket.getPriority().getBulgarianText();
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    if (ticket.getLastChangeTime() != null) {
      this.lastChangeTime = ticket.getLastChangeTime().format(formatter);
    } else {
      this.lastChangeTime = LocalDateTime.of(2024, 3, 2, 15, 13).format(formatter);
    }
    this.creatorFirstName = ticket.getCreator().getFirstName();
    UserDetailsImpl processor = ticket.getProcessor();
    if (processor != null) {
      this.processorFirstName = processor.getFirstName();
    }
    this.stateEn = ticket.getStatus().getEnglishText();
    this.stateBg = ticket.getStatus().getBulgarianText();
    
    this.messages = new ArrayList<>();
    
    if (ticket.getMessages() != null) {
      for (TicketMessage message : ticket.getMessages()) {
        messages.add(new TicketMessageDTO(message));
      }
    }
  }
  
}
