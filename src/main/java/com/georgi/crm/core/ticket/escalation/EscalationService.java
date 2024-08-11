package com.georgi.crm.core.ticket.escalation;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EscalationService {
  
  private EscalationRepository escalationRepository;
  
  public Escalation saveEscalation(Escalation escalation) {
    return escalationRepository.save(escalation);
  }
  
  public Escalation getEscalationById(Long id) {
    Optional<Escalation> escalationRequest = escalationRepository.findById(id);
    return escalationRequest.orElse(null);
  }
  
  public List<Escalation> getAllEscalations() {
    return escalationRepository.findAll();
  }
  
  public void deleteEscalation(Long id) {
    escalationRepository.deleteById(id);
  }
  
}
