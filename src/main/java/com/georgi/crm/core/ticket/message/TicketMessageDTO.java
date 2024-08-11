package com.georgi.crm.core.ticket.message;

import java.time.format.DateTimeFormatter;

import com.georgi.crm.core.ticket.message.impl.file.FileMessage;
import com.georgi.crm.core.ticket.message.impl.text.TextMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketMessageDTO {
  
  private Long ticketId;
  private String messageType;
  private String content;
  private String filePath;
  private String sender;
  private String sendDate;
  
  public TicketMessageDTO(TicketMessage ticketMessage) {
    this.ticketId = ticketMessage.getTicket().getId();
    if (ticketMessage instanceof TextMessage) {
      this.messageType = "text";
      this.content = ((TextMessage) ticketMessage).getTextContent();
    } else {
      this.messageType = "file";
      this.content = ((FileMessage) ticketMessage).getFilePath();
    }
    
    this.sender = ticketMessage.getSender().getFirstName();
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    this.sendDate = ticketMessage.getTimestamp().format(formatter);
  }
  
}
