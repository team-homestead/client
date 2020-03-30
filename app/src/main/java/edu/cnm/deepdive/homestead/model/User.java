package edu.cnm.deepdive.homestead.model;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.net.URL;
import java.util.UUID;

public class User implements Favorites {

  @Expose
  private UUID id;

  @Expose
  private String name;

  @Expose
  private String phoneNumber;

  @Expose
  private String email;

  @Expose
  private String oauthKey;

  @Expose
  private Agency agency;

  @Expose
  private Integer familyUnitNumber;

  @Expose
  private URL href;

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getOauthKey() {
    return oauthKey;
  }

  public void setOauthKey(String oauthKey) {
    this.oauthKey = oauthKey;
  }

  public Agency getAgency() {
    return agency;
  }

  public void setAgency(Agency agency) {
    this.agency = agency;
  }

  public Integer getFamilyUnitNumber() {
    return familyUnitNumber;
  }

  public void setFamilyUnitNumber(Integer familyUnitNumber) {
    this.familyUnitNumber = familyUnitNumber;
  }

  public URL getHref() {
    return href;
  }

  public void setHref(URL href) {
    this.href = href;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }

  public void setUser(UUID id) {

  }

  public void setDisplayName(String displayName) {
  }

}
