package edu.cnm.deepdive.homestead.model;

import com.google.gson.annotations.Expose;
import java.net.URL;
import java.util.UUID;

public class Agency implements Content {

/*  public Agency() {
    super();
  }

  public Agency(User[] users, AgencyType agencyType, Service[] services) {
    super();
    this.users = users;
    this.agencyType = agencyType;
    this.services = services;
  } */

  @Expose
  private UUID id;

  @Expose
  private String name;

  @Expose
  private String address;

  @Expose
  private String phoneNumber;

  @Expose
  private String email;

  @Expose
  private String description;

  @Expose
  private AgencyType agencyType;

  @Expose
  private User[] users;

  @Expose
  private Service[] services;

  @Expose
  private URL href;

  private boolean favorite;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AgencyType getAgencyType() {
    return agencyType;
  }

  public void setAgencyType(AgencyType agencyType) {
    this.agencyType = agencyType;
  }

  public User[] getUsers() {
    return users;
  }

  public void setUsers(User[] users) {
    this.users = users;
  }

  public Service[] getServices() {
    return services;
  }

  public void setServices(Service[] services) {
    this.services = services;
  }

  public URL getHref() {
    return href;
  }

  public void setHref(URL href) {
    this.href = href;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } if (obj == null) {
      return false;
    } if (getClass() != obj.getClass()) {
      return false;
    } Agency other = (Agency) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

 /* @Override
  public String toString() {
    return "Agency [name=" + name + ", address=" + address + ", phoneNumber=" + phoneNumber +
        ", email=" + email + ", agencyType=" + agencyType + ", services=" + services +
        ", description=" + description + "]";
  } */

  public enum AgencyType {
    GOVERNMENT, OTHER, PUBLIC, RELIGIOUS;

  }

}
