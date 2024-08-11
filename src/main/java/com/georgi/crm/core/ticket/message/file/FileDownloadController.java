package com.georgi.crm.core.ticket.message.file;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.georgi.crm.core.ticket.message.TicketMessageService;
import com.georgi.crm.core.ticket.message.impl.file.FileMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/api/download")
public class FileDownloadController {
  
  private final TicketMessageService ticketMessageService;
  
  @GetMapping
  public String getDownloadPage() {
    return "download";
  }
  
//  @GetMapping("/download")
//  public ResponseEntity<Resource> downloadFile(@RequestParam("messageId") Long messageId) {
//    FileMessage fileMessage = (FileMessage) ticketMessageService.findById(messageId);
//    if (fileMessage == null) {
//      return ResponseEntity.notFound().build();
//    }
//
//    Path filePath = Paths.get(fileMessage.getFilePath());
//    Resource resource;
//    try {
//      resource = new UrlResource(filePath.toUri());
//      if (!resource.exists() || !resource.isReadable()) {
//        throw new RuntimeException("Could not read the file!");
//      }
//    } catch (MalformedURLException e) {
//      throw new RuntimeException("Error: " + e.getMessage());
//    }
//
//    return ResponseEntity.ok()
//      .contentType(MediaType.parseMediaType("application/octet-stream"))
//      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//      .body(resource);
//  }
  
  @GetMapping("/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam("messageId") Long messageId) {
    FileMessage fileMessage = (FileMessage) ticketMessageService.findById(messageId);
    if (fileMessage == null) {
      return ResponseEntity.notFound().build();
    }
    
    Path filePath = Paths.get("src/main/resources/upload-dir").resolve(fileMessage.getFilePath()).normalize().toAbsolutePath();
    Resource resource;
    try {
      resource = new UrlResource(filePath.toUri());
      if (!resource.exists() || !resource.isReadable()) {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
    
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
      .body(resource);
  }
}