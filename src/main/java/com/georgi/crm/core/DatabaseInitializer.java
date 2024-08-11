package com.georgi.crm.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.georgi.crm.core.security.authr.MyAuthority;
import com.georgi.crm.core.security.authr.MyAuthorityRepository;
import com.georgi.crm.core.security.authr.MyDefinedAuthorities;
import com.georgi.crm.core.security.authr.MyDefinedRoles;
import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketRepository;
import com.georgi.crm.core.ticket.details.FacilityTicketDetails;
import com.georgi.crm.core.ticket.details.ProgrammingTicketDetails;
import com.georgi.crm.core.ticket.details.util.OperatingSystemType;
import com.georgi.crm.core.ticket.details.util.ProgrammingLanguage;
import com.georgi.crm.core.ticket.details.util.SystemType;
import com.georgi.crm.core.ticket.escalation.Escalation;
import com.georgi.crm.core.ticket.escalation.EscalationRepository;
import com.georgi.crm.core.ticket.message.TicketMessageService;
import com.georgi.crm.core.ticket.message.impl.text.TextMessage;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.message.TicketMessageRepository;
import com.georgi.crm.core.ticket.message.impl.text.TextMessageController;
import com.georgi.crm.core.ticket.util.TicketComponent;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.ticket.util.TicketStatus;
import com.georgi.crm.core.user.UserDetailsImpl;
import com.georgi.crm.core.user.UserDetailsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
  
  private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
  
  private final MyAuthorityRepository myAuthorityRepository;
  private final UserDetailsRepository userDetailsRepository;
  private final TicketRepository ticketRepository;
  private final TicketMessageRepository ticketMessageRepository;
  private final EscalationRepository escalationRepository;
  private final TextMessageController textMessageController;
  private final TicketMessageService ticketMessageService;
  
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public void run(String... args) throws Exception {
    List<UserDetailsImpl> users = userDetailsRepository.findAll();
    if (!users.isEmpty()) {
      System.out.println("DB already initialized.");
      return;
    }
    
    System.out.println("Begin initializing database with sample data");
    
    initAuthorities();
    
    initUsers();
    
    initTickets();
    
    initMessages();
    
    postInitMessage();
    
//    createTicketAndSendMessage();
    
    initEscalation();
    
    System.out.println("Database initialized with sample data");
  }
  

  
  private void initAuthorities() {
    List<MyAuthority> list = Arrays.asList(
      MyDefinedRoles.ROLE_ADMIN,
      MyDefinedRoles.ROLE_PROCESSOR,
      MyDefinedRoles.ROLE_CUSTOMER,
      MyDefinedRoles.ROLE_ESCAL_MGR,
      MyDefinedAuthorities.AUTHORITY_ADMIN,
      MyDefinedAuthorities.AUTHORITY_PROCESSOR,
      MyDefinedAuthorities.AUTHORITY_CUSTOMER,
      MyDefinedAuthorities.AUTHORITY_ESCAL_MGR
    );
    
    myAuthorityRepository.saveAll(list);
  }
  
  private void initUsers() {
    UserDetailsImpl admin = UserDetailsImpl.builder()
      .username("admin")
      .password(PASSWORD_ENCODER.encode("password"))
      .email("admin@adm.com")
      .secondEmail("georgi.petrov@adm.com")
      .firstName("Георги")
      .lastName("Петров")
      .phoneNumber("0812345678")
      .country("Bulgaria")
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_ADMIN, MyDefinedRoles.ROLE_ADMIN)))
      .userAttributes(new HashSet<>())
      .componentsResponsible(TicketComponent.getAll())
//      .componentsResponsible(new ArrayList<>())
      .build()
      .appendAttribute("Custom Attribute", "Custom Value"); // Other attributes could be left non-standardized
    
    UserDetailsImpl processor1 = UserDetailsImpl.builder()
      .username("processor_prog")
      .password(PASSWORD_ENCODER.encode("password"))
      .email("processor1@proc.com")
      .secondEmail("petar.georgiev@pro.com")
      .firstName("Петър")
      .lastName("Георгиев")
      .phoneNumber("0812345679")
      .country("Bulgaria")
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_PROCESSOR, MyDefinedRoles.ROLE_PROCESSOR)))
      .userAttributes(new HashSet<>())
      .componentsResponsible(Collections.singletonList(TicketComponent.PROG))
      .build();
    
    UserDetailsImpl processor2 = UserDetailsImpl.builder()
      .username("processor_hr")
      .email("processor2@proc.com")
      .secondEmail("petar.petrov@pro.com")
      .firstName("Павлин")
      .lastName("Петров")
      .phoneNumber("0812345681")
      .country("Bulgaria")
      .password(PASSWORD_ENCODER.encode("password"))
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_PROCESSOR, MyDefinedRoles.ROLE_PROCESSOR)))
      .userAttributes(new HashSet<>())
      .componentsResponsible(Collections.singletonList(TicketComponent.HR))
      .build();
    
    UserDetailsImpl processor3 = UserDetailsImpl.builder()
      .username("processor_facility")
      .email("processor3@proc.com")
      .secondEmail("georgi.georgiev@pro.com")
      .firstName("Данчо")
      .lastName("Кръстев")
      .phoneNumber("0812345682")
      .country("Bulgaria")
      .password(PASSWORD_ENCODER.encode("password"))
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_PROCESSOR, MyDefinedRoles.ROLE_PROCESSOR)))
      .userAttributes(new HashSet<>())
      .componentsResponsible(Collections.singletonList(TicketComponent.FAC))
      .build();
    
    UserDetailsImpl processor4 = UserDetailsImpl.builder()
      .username("processor_basic")
      .email("processor4@proc.com")
      .secondEmail("georgi.georgiev@pro.com")
      .firstName("Димитър")
      .lastName("Павлов")
      .phoneNumber("0812345683")
      .country("Bulgaria")
      .password(PASSWORD_ENCODER.encode("password"))
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_PROCESSOR, MyDefinedRoles.ROLE_PROCESSOR)))
      .userAttributes(new HashSet<>())
      .componentsResponsible(Collections.singletonList(TicketComponent.BASIC))
      .build();
    
    UserDetailsImpl processor5 = UserDetailsImpl.builder()
      .username("processor_equi")
      .email("processor5@proc.com")
      .secondEmail("georgi.georgiev@pro.com")
      .firstName("Атанас")
      .lastName("Атанасов")
      .phoneNumber("0812345684")
      .country("Bulgaria")
      .password(PASSWORD_ENCODER.encode("password"))
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_PROCESSOR, MyDefinedRoles.ROLE_PROCESSOR)))
      .userAttributes(new HashSet<>())
      .componentsResponsible(Collections.singletonList(TicketComponent.EQUI))
      .build();
    
    UserDetailsImpl customer1 = UserDetailsImpl.builder()
      .username("customer1")
      .password(PASSWORD_ENCODER.encode("password"))
      .email("customer1@cust.com")
      .secondEmail("avgustin.dekemvrijski@cus.com")
      .firstName("Августин")
      .lastName("Декемврийски")
      .phoneNumber("0812345680")
      .country("Tasmaniya")
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_CUSTOMER, MyDefinedRoles.ROLE_CUSTOMER)))
      .userAttributes(new HashSet<>())
      .build();
    
    UserDetailsImpl customer2 = UserDetailsImpl.builder()
      .username("customer2")
      .password(PASSWORD_ENCODER.encode("password"))
      .email("customer2@cust.com")
      .secondEmail("pedro.white@cus.com")
      .firstName("Pedro")
      .lastName("White")
      .phoneNumber("0812345690")
      .country("Mexico")
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_CUSTOMER, MyDefinedRoles.ROLE_CUSTOMER)))
      .userAttributes(new HashSet<>())
      .build();
    
    UserDetailsImpl escalationManager = UserDetailsImpl.builder()
      .username("escalation")
      .password(PASSWORD_ENCODER.encode("password"))
      .email("escalation@esc.com")
      .secondEmail("michael.felix@cus.com")
      .firstName("Michael")
      .lastName("Felist")
      .phoneNumber("0812345700")
      .country("USA")
      .authorities(new HashSet<>(Arrays.asList(MyDefinedAuthorities.AUTHORITY_ESCAL_MGR, MyDefinedRoles.ROLE_ESCAL_MGR)))
      .userAttributes(new HashSet<>())
      .build();
    
    List<UserDetailsImpl> users = List.of(
      admin,
      processor1, processor2, processor3, processor4, processor5,
      customer1, customer2,
      escalationManager);
    userDetailsRepository.saveAll(users);
  }
  
  private void initTickets() {
    // Get users
    UserDetailsImpl customer = userDetailsRepository.findByUsername("customer1").get();
    UserDetailsImpl customer2 = userDetailsRepository.findByUsername("customer2").get();
    UserDetailsImpl processor_prog = userDetailsRepository.findByUsername("processor_prog").get();
    UserDetailsImpl processor_hr = userDetailsRepository.findByUsername("processor_hr").get();
    UserDetailsImpl processor_equi = userDetailsRepository.findByUsername("processor_equi").get();
    UserDetailsImpl processor_facility = userDetailsRepository.findByUsername("processor_facility").get();
    
    // Create Details
    
//    Tickets
    
    TicketImpl ticket = new TicketImpl();
    ticket.setCreator(customer);
    ticket.setProcessor(processor_facility);
    
    List<TicketComponent> componentHistory = Arrays.asList(TicketComponent.BASIC, TicketComponent.FAC);
    ticket.setComponentHistory(componentHistory);
    ticket.setCurrentComponent(TicketComponent.FAC);
    ticket.setDescription("Развлено бюро в стая 17 на 3ти етаж.");
    ticket.setLastChangeTime(LocalDateTime.now().minusDays(1).minusHours(3));
    ticket = ticketRepository.save(ticket);
    
    FacilityTicketDetails facilityTicketDetails = new FacilityTicketDetails();
    facilityTicketDetails.setBuilding("SOF_01");
    facilityTicketDetails.setRoom(17);
    facilityTicketDetails.setFloor(3);
    facilityTicketDetails.setInfo("Развлено бюро в стая 17 на 3ти етаж.");
    facilityTicketDetails.setTicket(ticket);
    
    ticket.setDetails(facilityTicketDetails);
    ticket = ticketRepository.save(ticket);
    
    TicketImpl ticket2 = new TicketImpl();
    ticket2.setCreator(customer2);
    ticket2.setProcessor(processor_prog);
    ticket2.setComponentHistory(Arrays.asList(TicketComponent.BASIC, TicketComponent.PROG, TicketComponent.PROG_PYTHON));
    ticket2.setCurrentComponent(TicketComponent.PROG_PYTHON);
    ticket2.setDescription("Имаме проблем с рендерирането на Application Bar-а в нашия CRM Портал");
    ticket2.setLastChangeTime(LocalDateTime.now().minusHours(3));
    ticket2.setPriority(TicketPriority.VERY_HIGH);
    ticket2 = ticketRepository.save(ticket2);
    
    ProgrammingTicketDetails programmingTicketDetails = new ProgrammingTicketDetails();
    programmingTicketDetails.setTicket(ticket2);
    programmingTicketDetails.setOsType(OperatingSystemType.WINDOWS);
    programmingTicketDetails.setProgrammingLanguage(ProgrammingLanguage.PYTHON);
    
    ticket2.setDetails(programmingTicketDetails);
    ticket2 = ticketRepository.save(ticket2);
    
    TicketImpl ticket3 = new TicketImpl();
    ticket3.setCreator(customer);
    ticket3.setProcessor(processor_prog);
    ticket3.setComponentHistory(Arrays.asList(TicketComponent.BASIC, TicketComponent.PROG, TicketComponent.PROG_JAVA));
    ticket3.setCurrentComponent(TicketComponent.PROG_JAVA);
    ticket3.setDescription("Проблем с Java 21 инсталация");
    ticket3.setLastChangeTime(LocalDateTime.now().minusDays(2).minusHours(3));
    ticket3.setPriority(TicketPriority.LOW);
    ticket3 = ticketRepository.save(ticket3);
    
    ProgrammingTicketDetails programmingTicketDetails2 = new ProgrammingTicketDetails();
    programmingTicketDetails2.setTicket(ticket3);
    programmingTicketDetails2.setOsType(OperatingSystemType.LINUX);
    programmingTicketDetails2.setProgrammingLanguage(ProgrammingLanguage.JAVA);
    ticket3.setDetails(programmingTicketDetails2);
    ticket3 = ticketRepository.save(ticket3);
    
    
    TicketImpl ticket4 = new TicketImpl();
    ticket4.setCreator(processor_facility);
    ticket4.setProcessor(processor_facility);
    ticket4.setComponentHistory(Arrays.asList(TicketComponent.BASIC, TicketComponent.FAC_MAINTAIN));
    ticket4.setCurrentComponent(TicketComponent.FAC_MAINTAIN);
    ticket4.setDescription("Проблем с климатика в стая 9 на 1 етаж");
    ticket4.setLastChangeTime(LocalDateTime.now());
    ticket4.setPriority(TicketPriority.HIGH);
    ticket4 = ticketRepository.save(ticket4);
    
    FacilityTicketDetails facilityTicketDetails1 = new FacilityTicketDetails();
    facilityTicketDetails1.setTicket(ticket4);
    facilityTicketDetails1.setBuilding("SOF_01");
    facilityTicketDetails1.setRoom(9);
    facilityTicketDetails1.setFloor(1);
    facilityTicketDetails1.setInfo("Проблем с климатика в стая 9 на 1 етаж");
    ticket4.setDetails(facilityTicketDetails1);
    ticket4 = ticketRepository.save(ticket4);
    
    // ==================================================================
    
//    ticketRepository.saveAll(Arrays.asList(ticket, ticket2, ticket3, ticket4));
//    ticketRepository.save(ticket);
//    ticketRepository.save(ticket2);
//    ticketRepository.save(ticket3);
//    ticketRepository.save(ticket4);
  }
  
  private void initMessages() {
    // Get users
    UserDetailsImpl creator = userDetailsRepository.findByUsername("customer1").get();
    UserDetailsImpl processor = userDetailsRepository.findByUsername("processor_prog").get();
    
    // Get ticket
    TicketImpl ticket = ticketRepository.findByCreatorUsername("customer1").get(0);
    
    TextMessage textMessage1 = new TextMessage();
    textMessage1.setTextContent("This is the very first ticket message that I am creating. I have no issue. Cheers!");
    textMessage1.setSender(creator);
    textMessage1.setTimestamp(LocalDateTime.now());
    textMessage1.setTicket(ticket);
    
    TextMessage textMessage2 = new TextMessage();
    textMessage2.setTextContent("This is the very first Response. I'm happy that you don't have any issues. Cheers!");
    textMessage2.setSender(processor);
    textMessage2.setTimestamp(LocalDateTime.now());
    textMessage2.setTicket(ticket);
    
    ticketMessageRepository.saveAll(Arrays.asList(textMessage1, textMessage2));
  }
  
  private void postInitMessage() {
    // Get users
    UserDetailsImpl creator = userDetailsRepository.findByUsername("customer1").get();
    UserDetailsImpl processor = userDetailsRepository.findByUsername("processor_prog").get();
    
    // Get messages
    TicketMessage ticketMessage1 = ticketMessageRepository.findBySenderId(creator.getId()).get(0);
    TicketMessage ticketMessage2 = ticketMessageRepository.findBySenderId(processor.getId()).get(0);
    
    // Get ticket
    TicketImpl ticket = ticketRepository.findByCreatorUsername(creator.getUsername()).get(0);
    ticket.setMessages(Arrays.asList(ticketMessage1, ticketMessage2));
    
    ticketRepository.save(ticket);
  }
  //========== Attach a file via FileMessage to a ticket //==========
  // 1. get a ticket via TicketController to get the ticket id into the session.
  // localhost:8080/api/ticket/openTicket/1
  
  // 2. access the controller for creating a file message
  // localhost:8080/api/fileMessage/create
  
  //========== Download an attached file //==========
  // 1. open page for downloading files
  // localhost:8080/api/download
  
  // Make messages available for edit for 15 minutes after they're sent
  
  private void createTicketAndSendMessage() {
    UserDetailsImpl creator = userDetailsRepository.findByUsername("customer1").get();
    UserDetailsImpl processor = userDetailsRepository.findByUsername("processor1").get();
    
    ProgrammingTicketDetails details = ProgrammingTicketDetails
      .builder()
      .osType(OperatingSystemType.WINDOWS)
      .systemType(SystemType.CRM)
      .programmingLanguage(ProgrammingLanguage.PYTHON)
      .build();
      
    TicketImpl ticket = new TicketImpl();
    ticket.setCreator(creator);
    ticket.setProcessor(processor);
    
    List<TicketComponent> componentHistory = Arrays.asList(TicketComponent.BASIC, TicketComponent.PROG, TicketComponent.PROG_PYTHON);
    ticket.setComponentHistory(componentHistory);
    ticket.setCurrentComponent(TicketComponent.PROG_PYTHON);
    ticket.setDetails(details);
    ticket.setDescription("Hello, We have problem with Authenticating users in our CRM.");
    ticket.setStatus(TicketStatus.IN_PROCESS);
    ticket.setPriority(TicketPriority.VERY_HIGH);
    
//    textMessageController.logicalCreateTextMessage(new TextMessageDTO("Hello, please give more details"), 1L);
  }
  
  private void initEscalation() {
    TicketImpl ticket = ticketRepository.findByCreatorUsername("customer1").get(0);
    
    Escalation escalation = new Escalation(1L, false, ticket, LocalDateTime.now(), null);
    
    escalationRepository.save(escalation);
  }
}