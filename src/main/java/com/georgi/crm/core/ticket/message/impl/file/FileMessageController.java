package com.georgi.crm.core.ticket.message.impl.file;

import java.util.List;
import java.util.stream.Collectors;

import com.georgi.crm.core.SessionUtil;
import com.georgi.crm.core.security.UserDetailsServiceImpl;
import com.georgi.crm.core.ticket.TicketImpl;
import com.georgi.crm.core.ticket.TicketService;
import com.georgi.crm.core.ticket.message.TicketMessageService;
import com.georgi.crm.core.ticket.message.file.FileStorageService;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
@RequestMapping("/api/fileMessage")
public class FileMessageController {
  
  private final TicketMessageService ticketMessageService;
  private final TicketService ticketService;
  private final FileStorageService fileStorageService;
  
  @GetMapping("/create")
  public String getCreatePage() {
    return "upload";
  }
  
  @PostMapping("/create")
  public ResponseEntity<FileMessage> logicalCreateFileMessage(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
    UserDetailsImpl currentUser = UserDetailsServiceImpl.getCurrentUser();
    
    Long ticketId = SessionUtil.getCurrentTicketIdFromSession(httpSession);
    TicketImpl ticket = ticketService.getTicketById(ticketId);
    
    String folderName = "ticket_content_" + ticketId + "\\";
    String fileName = fileStorageService.storeFile(file, folderName);
    FileMessage fileMessage = new FileMessage(null, ticket, currentUser, folderName + fileName);
    
    return new ResponseEntity<>((FileMessage) ticketMessageService.logicalSave(fileMessage, ticket), HttpStatus.CREATED);
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<FileMessage> getFileMessage(@PathVariable Long id) {
    FileMessage message = (FileMessage) ticketMessageService.findById(id);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
  
  @GetMapping
  public ResponseEntity<List<FileMessage>> getAllFileMessages() {
    List<FileMessage> messages = ticketMessageService.findAll().stream()
      .filter(msg -> msg instanceof FileMessage)
      .map(msg -> (FileMessage) msg)
      .collect(Collectors.toList());
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFileMessage(@PathVariable Long id) {
    ticketMessageService.delete(id);
    return ResponseEntity.noContent().build();
  }
}