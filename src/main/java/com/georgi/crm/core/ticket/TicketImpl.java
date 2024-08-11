package com.georgi.crm.core.ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.georgi.crm.core.ticket.details.TicketDetails;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.ticket.util.TicketStatus;
import com.georgi.crm.core.ticket.util.TicketComponent;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class TicketImpl implements Ticket {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "creator_id")
  private UserDetailsImpl creator;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "processor_id")
  private UserDetailsImpl processor;

  @OneToOne(cascade = CascadeType.ALL)
  private TicketDetails details;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "current_component")
  private TicketComponent currentComponent = TicketComponent.BASIC;
  
  @ElementCollection(targetClass = TicketComponent.class)
  @CollectionTable(name = "ticket_component_history", joinColumns = @JoinColumn(name = "ticket_id"))
  @Column(name = "component")
  @Enumerated(EnumType.STRING)
  @OrderColumn(name = "component_order")
  private List<TicketComponent> componentHistory = new ArrayList<>();
  
  @Enumerated(EnumType.STRING)
  private TicketPriority priority = TicketPriority.VERY_LOW;
  
  @Enumerated(EnumType.STRING)
  private TicketStatus status = TicketStatus.NEW;
  
  private String description = "Description is empty";
  
  private LocalDateTime lastChangeTime;
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ticket")
  private List<TicketMessage> messages = new ArrayList<>();
  
  @Override
  public String toString() {
    return "<br>TicketImpl{" +
      "<br>id=" + id +
      ",<br> creator=" + creator +
      ",<br> processor=" + processor +
      ",<br> details=" + details.toString() +
      ",<br> currentComponent=" + currentComponent +
      ",<br> componentHistory=" + componentHistory +
      ",<br> priority=" + priority +
      ",<br> status=" + status +
      ",<br> description='" + description + '\'' +
      ",<br> messages=" + messages +
      '}';
  }
}
