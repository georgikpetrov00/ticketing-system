package com.georgi.crm.core.user;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_attribute")
public class UserAttribute {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "attr_key")
  private String attrKey;
  
  @Column(name = "attr_value")
  private String attrValue;
  
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserDetailsImpl user;
  
  public UserAttribute(String attrKey, String attrValue, UserDetailsImpl user) {
    this.attrKey = attrKey;
    this.attrValue = attrValue;
    this.user = user;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserAttribute that = (UserAttribute) o;
    return Objects.equals(attrKey, that.attrKey);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(attrKey);
  }
  
  @Override
  public String toString() {
    return "UserAttribute{" + attrKey + ":" + attrValue + '}';
  }
}