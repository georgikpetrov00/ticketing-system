package com.georgi.crm.core.ticket.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TicketComponent {
  
  // Basic
  BASIC,
  
  // Programming
  PROG,
  PROG_JAVA,
  PROG_PYTHON,
  PROG_CPP,
  
  // HR
  HR,
  HR_DOCUMENTS,
  HR_TRAINING,
  
  // IT
  EQUI,
  EQUI_HARDWARE,
  EQUI_LAPTOP,
  EQUI_MONITOR,
  EQUI_PC,
  
  // Facility
  FAC,
  FAC_CLEAN,
  FAC_FURNITURE,
  FAC_MAINTAIN,
  FAC_PARKING,
  FAC_SEC,
  FAC_UPG,
  FAC_UTIL;
  
  public static List<TicketComponent> getProgramming() {
    return new ArrayList<>(Arrays.asList(
      PROG,
      PROG_JAVA,
      PROG_PYTHON,
      PROG_CPP
    ));
  }
  
  public static TicketComponent[] getHR() {
    return new TicketComponent[]{
      HR,
      HR_DOCUMENTS,
      HR_TRAINING
    };
  }
  
  public static TicketComponent[] getEquipment() {
    return new TicketComponent[]{
      EQUI,
      EQUI_HARDWARE,
      EQUI_LAPTOP,
      EQUI_MONITOR,
      EQUI_PC
    };
  }
  
  public static TicketComponent[] getFacility() {
    return new TicketComponent[]{
      FAC,
      FAC_CLEAN,
      FAC_FURNITURE,
      FAC_MAINTAIN,
      FAC_PARKING,
      FAC_SEC,
      FAC_UPG,
      FAC_UTIL
    };
  }
  
  public static List<TicketComponent> getRoot() {
    return Arrays.asList(
      BASIC,
      PROG,
      HR,
      EQUI,
      FAC
    );
  }
  
  public static List<TicketComponent> getAll() {
    return Arrays.asList(TicketComponent.values());
  }
  
  @Override
  public String toString() {
    return name();
  }
}
