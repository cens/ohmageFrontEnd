package edu.ucla.cens.mobilize.client.dataaccess.awdataobjects;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/*
 * {"users":{"user.adv.pa":"restricted","user.adv.su":"privileged"},"name":"BH_HS_CS102_Spring_2011"}
 */

public class ClassAwData extends JavaScriptObject {
  protected ClassAwData() {};
  
  public final native String getName() /*-{ return this.name; }-*/;
  
  public final native JsArrayString getUserNames() /*-{
    var usernames = [];
    for (var username in this.users) {
      usernames.push(username);
    }
    return usernames;
  }-*/;

  public final native String getUserRole(String username) /*-{
    return this.users[username];
  }-*/;
  
  public final Map<String, String> getUserNameToUserRoleMap() {
    Map<String, String> userNameToUserRoleMap = new HashMap<String, String>();
    JsArrayString usernames = getUserNames();
    for (int i = 0; i < usernames.length(); i++) {
      String username = usernames.get(i);
      String role = getUserRole(username);
      userNameToUserRoleMap.put(username, role);
    }
    return userNameToUserRoleMap;
  }
  
  /*
  public final native JsArrayString getPrivilegedUsers() /*-{
    var users = [];
    for (var username in this.users) {
      if (this.users[username] == "privileged") users.push(username);
    }
    return users;
  }-*/;
  
  /*
  public final native JsArrayString getRestrictedUsers() /*-{
    var users = [];
    for (var username in this.users) {
      if (this.users[username] == "restricted") users.push(username);
    }
    return users;
  }-*/;
  
  // Create a ClassAwData from a JSON string
  public static native ClassAwData fromJsonString(String jsonString) /*-{
      return eval('(' + jsonString + ')'); 
  }-*/;
  
}
