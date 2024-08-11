package com.georgi.crm.core.ticket.message.impl.text;

import java.time.LocalDateTime;

import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("TEXT")
public class TextMessage extends TicketMessage {
  
  private String textContent;
  
  public TextMessage(Long id, TicketImpl ticket, UserDetailsImpl user, String textContent) {
    super(id, ticket, user, LocalDateTime.now());
    this.textContent = textContent;
  }
  
  @Override
  public String toString() {
    return "TextMessage{" +
      "textContent='" + textContent + '\'' +
      ", id=" + id +
      ", ticket=" + ticket.getId() +
      ", sender=" + sender.getUsername() +
      ", timestamp=" + timestamp +
      '}';
  }
}
