package com.georgi.crm.core.ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.util.TicketComponent;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.ticket.util.TicketStatus;
import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TicketService {
  
  private static final List<TicketStatus> ACTIVE_STATUSES = List.of(TicketStatus.NEW, TicketStatus.IN_PROCESS, TicketStatus.AWAITING_INFO);
  
  private final TicketRepository ticketRepository;
  private final UserDetailsService userDetailsService;
  
  public List<TicketImpl> getAllTickets() {
    return ticketRepository.findAll();
  }
  
  public TicketImpl getTicketById(Long id) {
    return ticketRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Ticket not found"));
  }
  
  public TicketImpl save(TicketImpl ticket) {
    return ticketRepository.save(ticket);
  }
  
  public List<TicketImpl> getTicketsByCreator(String username) {
    return ticketRepository.findByCreatorUsername(username);
  }
  
  public List<TicketImpl> getTicketsByProcessor(String username) {
    return ticketRepository.findByProcessorUsername(username);
  }
  
//  public List<TicketMessage> getForQueue() {
//    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
//    System.out.println(currentUser.getUsername());
//    return new ArrayList<>();
//  }
  
  public TicketImpl createTicket(TicketImpl ticket) {
    return ticketRepository.save(ticket);
  }
  
  // ========== Update Ticket
  
  public TicketImpl updateTicket(Long id, TicketImpl updatedTicket) {
    TicketImpl ticket = getTicketById(id);
    ticket.setProcessor(updatedTicket.getProcessor());
    ticket.setCurrentComponent(updatedTicket.getCurrentComponent());
    ticket.setComponentHistory(updatedTicket.getComponentHistory());
    ticket.setMessages(updatedTicket.getMessages());
    setTicketLastUpdate(ticket);
    
    ticketRepository.save(ticket);
    System.out.println("Ticket with ID=" + ticket.getId() + " updated.");
    return ticket;
  }
  
  public TicketImpl updateTicketProcessor(Long ticketId, Long newProcessorId) {
    TicketImpl ticket = getTicketById(ticketId);
    UserDetailsImpl user = userDetailsService.getUserById(newProcessorId);
    ticket.setProcessor(user);
    setTicketLastUpdate(ticket);
    
    ticketRepository.save(ticket);
    System.out.println("Ticket with ID=" + ticket.getId() + " updated. New Processor ID=" + newProcessorId + ".");
    return ticket;
  }
  
  public TicketImpl updateTicketDescription(Long ticketId, String newDescription) {
    TicketImpl ticket = getTicketById(ticketId);
    ticket.setDescription(newDescription);
    setTicketLastUpdate(ticket);
    
    ticketRepository.save(ticket);
    System.out.println("Ticket with ID=" + ticket.getId() + " updated. New Description=\"" + newDescription + "\".");
    return ticket;
  }
  
  public TicketImpl updateTicketComponent(Long ticketId, TicketComponent newComponent) {
    TicketImpl ticket = getTicketById(ticketId);
    ticket.setCurrentComponent(newComponent);
    ticket.getComponentHistory().add(newComponent);
    setTicketLastUpdate(ticket);
    
    ticketRepository.save(ticket);
    System.out.println("Ticket with ID=" + ticket.getId() + " updated. New Component=" + newComponent + ".");
    return ticket;
  }
  
  public TicketImpl updateTicketPriority(Long ticketId, TicketPriority newPriority) {
    TicketImpl ticket = getTicketById(ticketId);
    ticket.setPriority(newPriority);
    setTicketLastUpdate(ticket);
    
    ticketRepository.save(ticket);
    System.out.println("Ticket with ID=" + ticket.getId() + " updated. New Priority=" + newPriority + ".");
    return ticket;
  }
  
  public TicketImpl updateTicketStatus(Long ticketId, TicketStatus newStatus) {
    TicketImpl ticket = getTicketById(ticketId);
    ticket.setStatus(newStatus);
    setTicketLastUpdate(ticket);
    
    ticketRepository.save(ticket);
    System.out.println("Ticket with ID=" + ticket.getId() + " updated. New Status=" + newStatus + ".");
    return ticket;
  }
  
  public TicketImpl addTicketMessage(Long ticketId, TicketMessage ticketMessage) {
    TicketImpl ticket = getTicketById(ticketId);
    return addTicketMessage(ticket, ticketMessage);
  }
  
  public TicketImpl addTicketMessage(TicketImpl ticket, TicketMessage ticketMessage) {
    if (ticketMessage != null) {
      ticket.getMessages().add(ticketMessage);
      setTicketLastUpdate(ticket);
      
      ticket = ticketRepository.save(ticket);
      System.out.println("Ticket with ID=" + ticket.getId() + " updated. New Message added from User with ID=" + ticket.getCreator().getId() + ".");
    }
    
    return ticket;
  }
  
  public void deleteTicket(Long id) {
    ticketRepository.deleteById(id);
  }
  
  // ========== Advanced
  
//  @Transactional(readOnly = true)
  public List<TicketImpl> getTicketsResponsibleFor(UserDetailsImpl user) {
    List<TicketStatus> allStatuses = List.of(TicketStatus.NEW, TicketStatus.IN_PROCESS, TicketStatus.AWAITING_INFO);
    List<TicketComponent> components = user.getComponentsResponsible();
    Specification<TicketImpl> spec = Specification.where(TicketSpecifications.hasProcessor(user))
      .or(TicketSpecifications.hasCurrentComponents(components))
      .and(TicketSpecifications.hasStatusIn(allStatuses));
    return ticketRepository.findAll(spec);
  }
  
  public List<TicketImpl> getTicketsCreatedBy(UserDetailsImpl user) {
    Specification<TicketImpl> spec = Specification.where(TicketSpecifications.hasCreator(user))
      .and(TicketSpecifications.hasStatusIn(ACTIVE_STATUSES));
    return ticketRepository.findAll(spec);
  }
  
  public List<TicketImpl> getAllTicketsWithPriority(TicketPriority priority) {
    Specification<TicketImpl> spec = Specification.where(TicketSpecifications.hasPriority(priority));
    return ticketRepository.findAll(spec);
  }
  
  public List<TicketImpl> getActiveTicketsWithPriority(TicketPriority priority) {
    Specification<TicketImpl> spec = Specification.where(TicketSpecifications.hasPriority(priority))
      .and(TicketSpecifications.hasStatusIn(ACTIVE_STATUSES));
    return ticketRepository.findAll(spec);
  }
  
  public List<TicketImpl> getAllActiveTickets() {
    Specification<TicketImpl> spec = Specification.where(TicketSpecifications.hasStatusIn(ACTIVE_STATUSES));
    return ticketRepository.findAll(spec);
  }
  
  private void setTicketLastUpdate(TicketImpl ticket) {
    if (ticket != null) {
      ticket.setLastChangeTime(LocalDateTime.now());
    }
  }
  
  public TicketImpl unassignProcessor(Long ticketId) {
    Optional<TicketImpl> ticketOpt = ticketRepository.findById(ticketId);
    
    if (!ticketOpt.isPresent()) {
      return null;
    }
    
    TicketImpl ticket = ticketOpt.get();
    ticket.setProcessor(null);
    TicketImpl saved = ticketRepository.save(ticket);
    
    return saved;
  }
}