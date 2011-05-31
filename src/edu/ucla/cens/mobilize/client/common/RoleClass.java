package edu.ucla.cens.mobilize.client.common;

public enum RoleClass {
  PRIVILEGED, RESTRICTED;
  
  public String toUserFriendlyString() {
    return this.toString().substring(0, 1).concat(this.toString().substring(1).toLowerCase());
  }

  public String toServerString() {
    return this.toString().toLowerCase();
  }
}