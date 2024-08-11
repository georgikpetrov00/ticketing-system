package com.georgi.crm.core.ticket.message.impl.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class FileMessageDTO {
  
  private String fileName;
  
}
