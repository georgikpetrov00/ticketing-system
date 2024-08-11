package com.georgi.crm.core.ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.georgi.crm.core.SessionUtil;
import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.details.TicketDetails;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.message.TicketMessageDTO;
import com.georgi.crm.core.ticket.message.TicketMessageService;
import com.georgi.crm.core.ticket.message.file.FileStorageService;
import com.georgi.crm.core.ticket.message.impl.file.FileMessage;
import com.georgi.crm.core.ticket.message.impl.text.TextMessage;
import com.georgi.crm.core.ticket.util.Constants;
import com.georgi.crm.core.ticket.util.TicketComponent;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.ticket.util.TicketStatus;
import com.georgi.crm.core.user.UserDetailsDto;
import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ticket")
public class TicketController {
  
  private final TicketService ticketService;
  private final UserDetailsService userDetailsService;
  private final TicketMessageService ticketMessageService;
  private final FileStorageService fileStorageService;
  
  @GetMapping
  public ResponseEntity<List<TicketDTO>> getAllTickets() {
    List<TicketImpl> allTickets = ticketService.getAllTickets();
    List<TicketDTO> dtos = new ArrayList<>();
    for (TicketImpl ticket : allTickets) {
      dtos.add(new TicketDTO(ticket));
    }
    
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }
  
  @GetMapping("/with-my-involvement")
  public ResponseEntity<List<TicketDTO>> getRelevantForCurrentUser() {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    
    List<TicketImpl> tickets = ticketService.getTicketsResponsibleFor(currentUser);
    List<TicketDTO> dtos = new ArrayList<>();
    
    if (tickets != null) {
      for (TicketImpl ticket : tickets) {
        dtos.add(new TicketDTO(ticket));
      }
    }
    
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }
  
  @GetMapping("/created-by-me")
  public ResponseEntity<List<TicketDTO>> getCreatedByCurrentUres() {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    
    List<TicketImpl> tickets = ticketService.getTicketsCreatedBy(currentUser);
    List<TicketDTO> dtos = new ArrayList<>();
    
    if (tickets != null) {
      for (TicketImpl ticket : tickets) {
        dtos.add(new TicketDTO(ticket));
      }
    }
    
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }
  
  @GetMapping("/{id}")
  public TicketDTO getTicketById(@PathVariable Long id) {
    return new TicketDTO(ticketService.getTicketById(id));
  }
  
  @GetMapping("/toString/{id}")
  public String getTicketToStringById(@PathVariable Long id) {
    return ticketService.getTicketById(id).toString();
  }
  
  @PostMapping
  public ResponseEntity<TicketImpl> createTicket(@RequestBody TicketImpl ticket) {
    TicketImpl createdTicket = ticketService.createTicket(ticket);
    return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
  }
  
  @PutMapping("/{id}")
  public TicketImpl updateTicket(@PathVariable Long id, @RequestBody TicketImpl updatedTicket) {
    return ticketService.updateTicket(id, updatedTicket);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
    ticketService.deleteTicket(id);
    return ResponseEntity.noContent().build();
  }
  
  // ========= Advanced
  
  @GetMapping("/openTicket/{ticketId}")
  @ResponseBody
  public String logicalOpenTicket(@PathVariable Long ticketId, HttpSession session, Model model) {
    TicketImpl ticket = ticketService.getTicketById(ticketId);
    model.addAttribute("ticket", ticket);
    
    session.setAttribute(Constants.SESSION_CURRENT_TICKET, ticketId);
    
    return "ticketDetails";
  }
  
  @PostMapping("/create")
  public ResponseEntity<TicketDTO> logicalCreateTicket(@RequestBody TicketDTO ticketDTO) {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    
    
    TicketImpl ticket = new TicketImpl();
    ticket.setCreator(currentUser);
    ticket.setProcessor(null);
    ticket.setStatus(TicketStatus.NEW);
    ticket.setDescription(ticketDTO.getDescription());
    ticket.setCurrentComponent(TicketComponent.valueOf(ticketDTO.getCurrentComponent()));
    TicketDetails details = ticketDTO.getDetails();
    details.setTicket(ticket);
    ticket.setDetails(details);
    ticket.setComponentHistory(Arrays.asList(TicketComponent.valueOf(ticketDTO.getCurrentComponent())));
    ticket.setPriority(TicketPriority.bulgarianToConstant(ticketDTO.getPriority()));
    ticket.setMessages(new ArrayList<>());
    ticket.setLastChangeTime(LocalDateTime.now());
    
    ticket = ticketService.createTicket(ticket);
    return new ResponseEntity<>(new TicketDTO(ticket), HttpStatus.CREATED);
  }
  
  @PostMapping("/sendReply")
  public ResponseEntity<TicketMessageDTO> sendReply(@RequestBody TicketMessageDTO messageDTO) {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    TicketImpl ticket = ticketService.getTicketById(messageDTO.getTicketId());
    
    if (TicketStatus.RESOLVED.equals(ticket.getStatus())) {
      System.out.println("Ticket is resolved. Unable to send reply.");
      return ResponseEntity.badRequest().build();
    }
    
    TicketMessage ticketMessage;
    if ("TEXT".equalsIgnoreCase(messageDTO.getMessageType())) {
      String content = messageDTO.getContent();
      ticketMessage = new TextMessage(null, ticket, currentUser, content);
    } else if ("FILE".equalsIgnoreCase(messageDTO.getMessageType())) {
      MultipartFile file = null;
      String filePath = fileStorageService.storeFile(file, "ticket_content_" + ticket.getId());
      ticketMessage = new FileMessage(null, ticket, currentUser, filePath);
    } else {
      return ResponseEntity.badRequest().build();
    }
    
    if (ticket.getProcessor() == null) {
      ticket.setProcessor(currentUser);
    }
    
    ticketMessageService.save(ticketMessage);
    
    if (ticket.getCreator().equals(currentUser)) {
      ticket.setStatus(TicketStatus.IN_PROCESS);
    } else {
      ticket.setStatus(TicketStatus.AWAITING_INFO);
    }
    
    ticketService.updateTicket(ticket.getId(), ticket);
    
    return new ResponseEntity<>(new TicketMessageDTO(ticketMessage), HttpStatus.OK);
  }
  
  @PostMapping("/resolve")
  public ResponseEntity<TicketImpl> resolveTicket(@RequestParam Long ticketId) {
    
    TicketImpl ticket = ticketService.getTicketById(ticketId);
    if (ticket == null) {
      return ResponseEntity.notFound().build();
    }
    
    ticket.setStatus(TicketStatus.RESOLVED);
    TicketImpl updatedTicket = ticketService.updateTicket(ticketId, ticket);
    
    return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
  }
  
  @GetMapping("/creator/{username}")
  public List<TicketImpl> getTicketsByCreator(@PathVariable String username) {
    return ticketService.getTicketsByCreator(username);
  }
  
  @GetMapping("/processor/{username}")
  public List<TicketImpl> getTicketsByProcessor(@PathVariable String username) {
    return ticketService.getTicketsByProcessor(username);
  }
  
  @GetMapping("/createdBy")
  public ResponseEntity<List<TicketImpl>> getCreatedBy() {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    
    return ResponseEntity.ok(ticketService.getTicketsCreatedBy(currentUser));
  }
  
  @PutMapping("/assignToMe")
  public ResponseEntity<TicketDTO> assignToMe(@RequestParam Long ticketId) {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    
    return updateTicketProcessor(ticketId, currentUser.getId());
  }
  
  @PutMapping("/unassign")
  public ResponseEntity<TicketDTO> unassign(@RequestParam Long ticketId) {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    currentUser = userDetailsService.getUserByUsername(currentUser.getUsername());
    
    TicketImpl ticket = ticketService.unassignProcessor(ticketId);
    
    if (ticket != null) {
      return new ResponseEntity<>(new TicketDTO(ticket), HttpStatus.OK);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
  // PUT /tickets/updateProcessor?ticketId=1&newProcessorId=2
  @PutMapping("/updateProcessor")
  public ResponseEntity<TicketDTO> updateTicketProcessor(
    @RequestParam Long ticketId,
    @RequestParam Long newProcessorId) {
    TicketImpl updatedTicket = ticketService.updateTicketProcessor(ticketId, newProcessorId);
    return ResponseEntity.ok(new TicketDTO(updatedTicket));
  }
  
  @PutMapping("/updateDescription")
  public ResponseEntity<TicketDTO> updateTicketDescription(
    @RequestParam Long ticketId,
    @RequestParam String newDescription) {
    TicketImpl updatedTicket = ticketService.updateTicketDescription(ticketId, newDescription);
    return ResponseEntity.ok(new TicketDTO(updatedTicket));
  }
  
  @PutMapping("/updateComponent")
  public ResponseEntity<TicketDTO> updateTicketComponent(
    @RequestParam Long ticketId,
    @RequestParam String newComponent) {
    TicketComponent componentEnum = TicketComponent.valueOf(newComponent.toUpperCase());
    TicketImpl updatedTicket = ticketService.updateTicketComponent(ticketId, componentEnum);
    return ResponseEntity.ok(new TicketDTO(updatedTicket));
  }
  
  @PutMapping("/updatePriority")
  public ResponseEntity<TicketDTO> updateTicketPriority(
    @RequestParam Long ticketId,
    @RequestParam String newPriority) {
    TicketPriority priorityEnum = TicketPriority.valueOf(newPriority.toUpperCase());
    TicketImpl updatedTicket = ticketService.updateTicketPriority(ticketId, priorityEnum);
    return ResponseEntity.ok(new TicketDTO(updatedTicket));
  }
  
  @PutMapping("/updateStatus")
  public ResponseEntity<TicketImpl> updateTicketStatus(
    @RequestParam Long ticketId,
    @RequestParam String newStatus) {
    TicketStatus statusEnum = TicketStatus.valueOf(newStatus.toUpperCase());
    TicketImpl updatedTicket = ticketService.updateTicketStatus(ticketId, statusEnum);
    return ResponseEntity.ok(updatedTicket);
  }
  
  @PostMapping("/addMessage")
  public ResponseEntity<TicketImpl> updateTicketAddMessage(
    @RequestParam Long ticketId,
    @RequestBody TicketMessage ticketMessage) {
    TicketImpl ticket = ticketService.addTicketMessage(ticketId, ticketMessage);
    return ResponseEntity.ok(ticket);
  }
  
  @GetMapping("/components")
  public ResponseEntity<List<String>> getComponentsList() {
    List<String> components = new ArrayList<>();
    
    for (TicketComponent component : TicketComponent.getAll()) {
      components.add(component.name());
    }
    return new ResponseEntity<>(components, HttpStatus.OK);
  }
  
  @GetMapping("/priorities")
  public ResponseEntity<List<TicketPriority>> getPrioritiesList() {
    return new ResponseEntity<>(TicketPriority.getAll(), HttpStatus.OK);
  }
  
  @GetMapping("/priorities-bg")
  public ResponseEntity<List<String>> getPrioritiesListBg() {
    List<String> priorities = new ArrayList<>();

    for (TicketPriority priority : TicketPriority.getAll()) {
      priorities.add(priority.getBulgarianText());
    }
    
    return new ResponseEntity<>(priorities, HttpStatus.OK);
  }
  
  @GetMapping("/all-tickets-count")
  public ResponseEntity<Integer> getAllTicketsCount() {
    return new ResponseEntity<>(ticketService.getAllTickets().size(), HttpStatus.OK);
  }
  
  @GetMapping("/all-tickets-count-with-priority")
  public ResponseEntity<Integer> getAllTicketsCountWithPriority(@RequestParam TicketPriority priority) {
    return new ResponseEntity<>(ticketService.getAllTicketsWithPriority(priority).size(), HttpStatus.OK);
  }
  
  @GetMapping("/active-tickets-count-with-priority")
  public ResponseEntity<Integer> getActiveTicketsCountWithPriority(@RequestParam TicketPriority priority) {
    return new ResponseEntity<>(ticketService.getActiveTicketsWithPriority(priority).size(), HttpStatus.OK);
  }
  
  @GetMapping("/statistics-all-per-component")
  public ResponseEntity<Map<String, Integer>> getStatisticsAllPerComponent() {
    Map<String, Integer> countPerComponent = new HashMap<>();
    for (TicketComponent component : TicketComponent.getAll()) {
      countPerComponent.put(component.toString(), 0);
    }
    
    List<TicketImpl> allTickets = ticketService.getAllTickets();
    
    for (TicketImpl ticket : allTickets) {
      TicketComponent ticketComponent = ticket.getCurrentComponent();
      int tempCount = countPerComponent.get(ticketComponent.toString());
      countPerComponent.put(ticketComponent.toString(), tempCount + 1);
    }
    
    return new ResponseEntity<>(countPerComponent, HttpStatus.OK);
  }
  
  @GetMapping("/statistics-active-per-component")
  public ResponseEntity<Map<String, Integer>> getStatisticsActivePerComponent() {
    Map<String, Integer> countPerComponent = new HashMap<>();
    for (TicketComponent component : TicketComponent.getAll()) {
      countPerComponent.put(component.toString(), 0);
    }
    
    List<TicketImpl> allActiveTickets = ticketService.getAllActiveTickets();
    
    for (TicketImpl ticket : allActiveTickets) {
      TicketComponent ticketComponent = ticket.getCurrentComponent();
      int tempCount = countPerComponent.get(ticketComponent.toString());
      countPerComponent.put(ticketComponent.toString(), tempCount + 1);
    }
    
    return new ResponseEntity<>(countPerComponent, HttpStatus.OK);
  }
}