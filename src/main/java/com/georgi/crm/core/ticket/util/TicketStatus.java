package com.georgi.crm.core.ticket.util;

public enum TicketStatus {
  
  AWAITING_INFO("Awaiting Information", "Очакване на информация"),
  CANCELED("Canceled", "Отменен"),
  IN_PROCESS("In Process", "В процес"),
  NEW("New", "Нов"),
  RESOLVED("Resolved", "Разрешен");
  
  private final String englishText;
  private final String bulgarianText;
  
  TicketStatus(String englishText, String bulgarianText) {
    this.englishText = englishText;
    this.bulgarianText = bulgarianText;
  }
  
  public String getEnglishText() {
    return englishText;
  }
  
  public String getBulgarianText() {
    return bulgarianText;
  }
}
