package com.georgi.crm.core.ticket;

import java.util.List;

import com.georgi.crm.core.ticket.util.TicketComponent;
import com.georgi.crm.core.ticket.util.TicketPriority;
import com.georgi.crm.core.ticket.util.TicketStatus;
import com.georgi.crm.core.user.UserDetailsImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecifications {
  
  public static Specification<TicketImpl> hasCreator(UserDetailsImpl user) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.or(criteriaBuilder.equal(root.get("creator"), user));
  }
  
  public static Specification<TicketImpl> hasProcessor(UserDetailsImpl user) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.or(criteriaBuilder.equal(root.get("processor"), user));
  }
  
  public static Specification<TicketImpl> hasCurrentComponent(TicketComponent component) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("currentComponent"), component);
  }
  
  public static Specification<TicketImpl> hasPriority(TicketPriority priority) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
  }
  
  public static Specification<TicketImpl> hasCurrentComponents(List<TicketComponent> components) {
    return (root, query, criteriaBuilder) -> {
      CriteriaBuilder.In<TicketComponent> inClause = criteriaBuilder.in(root.get("currentComponent"));
      for (TicketComponent component : components) {
        inClause.value(component);
      }
      return inClause;
    };
  }
  
  public static Specification<TicketImpl> hasStatusIn(List<TicketStatus> statuses) {
    return (root, query, criteriaBuilder) -> root.get("status").in(statuses);
  }
  
//  public static Specification<TicketImpl> createTicketSpecification(UserDetailsImpl user, List<TicketComponent> components, List<TicketStatus> statuses) {
//    return Specification.where(hasCreatorOrProcessor(user))
//      .and(hasCurrentComponents(components))
//      .and(hasStatusIn(statuses));
//  }
}