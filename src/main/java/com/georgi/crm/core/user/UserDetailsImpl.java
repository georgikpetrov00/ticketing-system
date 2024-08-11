package com.georgi.crm.core.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.georgi.crm.core.security.authr.MyAuthority;
import com.georgi.crm.core.ticket.escalation.Escalation;
import com.georgi.crm.core.ticket.message.TicketMessage;
import com.georgi.crm.core.ticket.util.TicketComponent;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserDetailsImpl implements UserDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  //========== User Attributes //==========
  @Column(name = "username")
  private String username;
  
  @Column(name = "password")
  private String password;
  
  @Column(name = "email")
  private String email;
  
  @Column(name = "second_email")
  private String secondEmail;
  
  @Column(name = "phone_number")
  private String phoneNumber;
  
  @Column(name = "country")
  private String country;
  
  @Column(name = "first_name")
  private String firstName;
  
  @Column(name = "last_name")
  private String lastName;
  
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserAttribute> userAttributes = new HashSet<>();
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sender")
  @JsonIgnore
  private List<TicketMessage> messages = new ArrayList<>();
  
  @ElementCollection
  @CollectionTable(name = "user_component_responsible",
    joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "component")
  @Enumerated(EnumType.STRING)
  private List<TicketComponent> componentsResponsible = new ArrayList<>();
  
  @OneToMany(mappedBy = "escalationManager", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Escalation> escalations = new ArrayList<>();
  
  //========== Systematic User Attributes //==========
  
  @Column(name = "is_account_non_expired")
  private final boolean isAccountNonExpired = true;
  
  @Column(name = "is_credentials_non_locked")
  private final boolean isAccountNonLocked = true;
  
  @Column(name = "is_credentials_non_expired")
  private final boolean isCredentialsNonExpired = true;
  
  @Column(name = "is_enabled")
  private final boolean isEnabled = true;
  
  @ManyToMany(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_authorities",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id"))
  private Set<MyAuthority> authorities = new HashSet<>();
  
  //========== End of User Attributes //==========
  
  public void addAttribute(UserAttribute attribute) {
    userAttributes.add(attribute);
    System.out.println("Attribute '" + attribute.getAttrKey() + ":" + attribute.getAttrValue() + "' added to user " + username);
  }
  
  public void addAttribute(String key, String value) {
    addAttribute(new UserAttribute(key, value, this));
  }
  
  public UserDetailsImpl appendAttribute(String key, String value) {
    addAttribute(key, value);
    return this;
  }
  
  public UserAttribute getAttribute(String key) {
    return userAttributes.stream()
      .filter(attribute -> attribute.getAttrKey().equalsIgnoreCase(key))
      .findFirst()
      .orElseThrow(() -> new RuntimeException("Attribute with key '" + key + " not found for user " + username));
  }
  
  public boolean hasAttribute(String key) {
    return userAttributes
      .stream()
      .anyMatch(attribute -> attribute.getAttrKey().equalsIgnoreCase(key));
  }
  
  public void removeAttribute(String key) {
    UserAttribute attributeToRemove = null;
    
    if (hasAttribute(key)) {
      attributeToRemove = getAttribute(key);
      userAttributes.remove(attributeToRemove);
    }
    
    if (attributeToRemove != null) {
      System.out.println("Attribute '" + key + ":" + attributeToRemove.getAttrValue() + "' removed from user " + username);
    }
  }

  
  public Set<MyAuthority> getAuthoritiez() {
    return authorities;
  }
  
  @Override
  public String toString() {
    return "<br>UserDetailsImpl{" +
      "<br>id=" + id +
      ",<br> username='" + username + '\'' +
      ",<br> email='" + email + '\'' +
      ",<br> secondEmail='" + secondEmail + '\'' +
      ",<br> phoneNumber='" + phoneNumber + '\'' +
      ",<br> country='" + country + '\'' +
      ",<br> firstName='" + firstName + '\'' +
      ",<br> lastName='" + lastName + '\'' +
      ",<br> userAttributes=" + userAttributes +
      ",<br> messages=" + messages +
      ",<br> isAccountNonExpired=" + isAccountNonExpired +
      ",<br> isAccountNonLocked=" + isAccountNonLocked +
      ",<br> isCredentialsNonExpired=" + isCredentialsNonExpired +
      ",<br> isEnabled=" + isEnabled +
      ",<br> authorities=" + authorities +
      '}';
  }
}
