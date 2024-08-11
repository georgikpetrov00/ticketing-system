package com.georgi.crm.core.ticket.message.impl.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ImageMessageDTO {
  
  private String imageFileName;
  
}
