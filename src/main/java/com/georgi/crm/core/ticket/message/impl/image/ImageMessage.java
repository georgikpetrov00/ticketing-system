package com.georgi.crm.core.ticket.message.impl.image;

import java.time.LocalDateTime;

import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("IMAGE")
public class ImageMessage extends TicketMessage {
  
  private String imageFileName;
  
  public ImageMessage(Long id, TicketImpl ticket, UserDetailsImpl user, String imageFileName) {
    super(id, ticket, user, LocalDateTime.now());
    this.imageFileName = imageFileName;
  }
  
}