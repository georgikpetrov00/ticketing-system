package com.georgi.crm.core.ticket.message.impl.text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TextMessageDTO {
  
  private String textContent;
  
}
