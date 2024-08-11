package com.georgi.crm.core.ticket.message.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.georgi.crm.core.ticket.message.TicketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
  
  private final Path fileStorageLocation;
  private final TicketMessageService ticketMessageService;
  
  @Autowired
  public FileStorageService(FileStorageProperties fileStorageProperties, TicketMessageService ticketMessageService) {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
      .toAbsolutePath().normalize();
    
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
    
    this.ticketMessageService = ticketMessageService;
  }
  
  @Transactional
  public String storeFile(MultipartFile file) {
    return storeFile(file, "ticket_content_1");
  }
  
  public String storeFile(MultipartFile file, String folderName) {
    // Normalize file name
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    
    try {
      // Check if the file's name contains invalid characters
      if (fileName.contains("..")) {
        throw new FileStorageException("Filename contains invalid path sequence - '..' - " + fileName);
      }
      
      Path ticketDir = Paths.get(fileStorageLocation.toString(), folderName);
      if (!Files.exists(ticketDir)) {
        Files.createDirectories(ticketDir);
      }
      
      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = this.fileStorageLocation.resolve(folderName + "/" + fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      
      System.out.println("File " + ticketDir + "//" + fileName + " stored successfully.");
      return fileName;
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
    }
  }
}