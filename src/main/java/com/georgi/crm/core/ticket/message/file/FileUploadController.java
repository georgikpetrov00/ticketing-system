package com.georgi.crm.core.ticket.message.file;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/upload")
@AllArgsConstructor
public class FileUploadController {
  
  private final FileStorageService fileStorageService;
  
  @PostMapping("/file")
  public ResponseEntity<String> uploadFile(@RequestParam("ticketId") Long ticketId, @RequestParam("file") MultipartFile file) {
    String fileName = fileStorageService.storeFile(file, "ticket_content_" + ticketId);
    return ResponseEntity.ok("File uploaded successfully: " + fileName);
  }
  
//  @PostMapping("/multipleFiles")
//  public ResponseEntity<String> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//    for (MultipartFile file : files) {
//      fileStorageService.storeFile(file);
//    }
//    return ResponseEntity.ok("Files uploaded successfully.");
//  }
  
  @GetMapping("/upload-form")
  public String showUploadForm() {
    return "upload";
  }
}