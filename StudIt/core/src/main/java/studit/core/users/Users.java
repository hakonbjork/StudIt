package studit.core.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Users {
  private Map<Integer, User> users = new HashMap<>();
  private int prevAssignedID = -1;

  public Map<Integer, User> getUsers() {
    return users;
  }

  public void setUsers(Map<Integer, User> users) {
    this.users = users;
  }

  public int getPrevAssignedID() {
    return prevAssignedID;
  }

  public void setPrevAssignedID(int prevAssignedID) {
    this.prevAssignedID = prevAssignedID;
  }

  /**
   * Adds a new user to the database.
   * 
   * @param name     name and surname
   * @param username unique username
   * @param mail     mail adress
   * @param password password (unencrypted)
   * @return HashMap key (ID) for the new User object if username is unique, else
   *         -1
   */
  public int addUser(String name, String username, String mail, String password) {
    if (isUnique(username)) {
      prevAssignedID += 1;
      users.put(prevAssignedID, new User(name, username, mail, password, prevAssignedID));
      return prevAssignedID;
    }
    return -1;
  }

  private boolean isUnique(String username) {
    for (User user : users.values()) {
      if (user.getUsername().equals(username)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Removes user from database.
   * 
   * @param uniqueID HashMap key (ID) for the User object to be removed
   * @return true if user was found and deleted, otherwise false
   */
  public boolean removeUser(int uniqueID) {
    User removedUser = users.remove(uniqueID);
    return removedUser == null ? false : true;
  }

  /**
   * Get the ID of a user by username.
   * @param username unique username
   * @return uniqueID if username in database, otherwise -1
   */
  private int getIDbyUsername(String username) {
    for (Entry<Integer, User> entry : users.entrySet()) {
      if (entry.getValue().getUsername().equals(username)) {
        return entry.getKey();
      }
    }
    return -1;
  } 

  /**
   * Authenticate login request.
   * @param username unique username of User
   * @param password password to check
   * @return true if valid login, false otherwise
   */
  public boolean authenticateLogin(String username, String password) {
    int uniqueID = getIDbyUsername(username);
    if (uniqueID == -1) {
      return false;
    }
    return users.get(uniqueID).getPassword().equals(password) ? true : false;
  }
}