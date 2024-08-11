package com.georgi.crm.core.ticket.message.impl.file;

import java.time.LocalDateTime;

import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("FILE")
public class FileMessage extends TicketMessage {
  
  private String filePath;
  
  public FileMessage(Long id, TicketImpl ticket, UserDetailsImpl user, String filePath) {
    super(id, ticket, user, LocalDateTime.now());
    this.filePath = filePath;
  }
}