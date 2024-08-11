package com.georgi.crm.core;

import com.georgi.crm.core.ticket.util.Constants;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
  
  public static Long getCurrentTicketIdFromSession(HttpSession httpSession) {
    Long currentTicketId;
    Object attribute = httpSession.getAttribute(Constants.SESSION_CURRENT_TICKET);
    
    if (attribute == null) {
      throw new RuntimeException("No Ticket attached to HttpSession object");
    }
    
    if (attribute instanceof Long) {
      currentTicketId = (Long) attribute;
    } else {
      throw new RuntimeException("Bad object stored in session attribute '" + Constants.SESSION_CURRENT_TICKET + "'. Expected Long, but found " + attribute.getClass().getName());
    }
  
    return currentTicketId;
  }
  
}
