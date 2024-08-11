package com.georgi.crm.core.ticket.util;

import java.util.Arrays;
import java.util.List;

public enum TicketPriority {
  
  VERY_LOW("Very Low", "Много нисък"),
  LOW("Low", "Нисък"),
  MEDIUM("Medium", "Среден"),
  HIGH("High", "Висок"),
  VERY_HIGH("Very High", "Много висок");
  
  private final String englishText;
  private final String bulgarianText;
  
  TicketPriority(String englishText, String bulgarianText) {
    this.englishText = englishText;
    this.bulgarianText = bulgarianText;
  }
  
  public String getEnglishText() {
    return englishText;
  }
  
  public String getBulgarianText() {
    return bulgarianText;
  }
  
  public static TicketPriority bulgarianToConstant(String bulgarianText) {
    return switch (bulgarianText) {
      case "Много нисък" -> VERY_LOW;
      case "Нисък" -> LOW;
      case "Среден" -> MEDIUM;
      case "Висок" -> HIGH;
      case "Много висок" -> VERY_HIGH;
      default -> VERY_LOW;
    };
  }
  
  public static List<TicketPriority> getAll() {
    return Arrays.asList(TicketPriority.values());
  }
}
