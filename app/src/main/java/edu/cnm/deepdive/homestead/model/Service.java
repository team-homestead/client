package edu.cnm.deepdive.homestead.model;

import com.google.gson.annotations.Expose;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class Service implements Favorites {

  @Expose
  private UUID id;

  @Expose
  private ServiceType serviceType;

  @Expose
  private Agency agency;

  @Expose
  private String notes;

  @Expose
  private URL href;

  public Agency getAgency() {
    return agency;
  }

  public void setAgency(Agency agency) {
    this.agency = agency;
  }

  public ServiceType getServiceType() {
    return serviceType;
  }

  public void setServiceType(ServiceType serviceType) {
    this.serviceType = serviceType;
  }

  public UUID getId() {
    return null;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public URL getHref() {
    return href;
  }

  public void setHref(URL href) {
    this.href = href;
  }

  public List<Service> getServices() {
    return getServices();
  }

  public void setService(Object o) {

  }

  public enum ServiceType {
    FOOD,
    SHELTER,
    CLOTHING,
    SUPPLIES;
  }
}
