package studit.core.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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
   * @return String[0] contains the new ID for the user if all checks were passed,
   *         otherwise null. String[1] contains the error message if something
   *         went wrong, null otherwise, String[2] contains the error code. 0 =
   *         new user created, -1 = missing fields, -2 = invalid username, -3 =
   *         invalid password
   */
  public String[] addUser(String name, String username, String mail, String password) {
    if (name == null || username == null || mail == null || password == null) {
      return new String[] { null, "Missing fields, expected name, username, mail and password", "-1" };
    }
    if (isUnique(username)) {
      if (!isValidEmailAddress(mail)) {
        return new String[] { null, "'" + mail + "'" + " is not a valid email address", "-3" };
      }

      String[] passwordHash = Hashing.hashPassword(password);
      // Invalid password
      if (passwordHash[0] == null) {
        return new String[] { null, passwordHash[1] };
      }
      prevAssignedID += 1;
      users.put(prevAssignedID, new User(name, username, mail, passwordHash[0], prevAssignedID));
      return new String[] { String.valueOf(prevAssignedID), null, "0" };
    }
    return new String[] { null, "'" + username + "' is not a unique username", "-2" };
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
   * Returns User object by id.
   * 
   * @param id uniqueID for the user
   * @return User if found, else null
   */
  public User getUserByID(int id) {
    return users.get(id);
  }

  /**
   * Get the ID of a user by username.
   * 
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
   * 
   * @param username unique username of User
   * @param password password to check
   * @return true if valid login, false otherwise
   */
  public boolean authenticateLogin(String username, String password) {
    int uniqueID = getIDbyUsername(username);
    if (uniqueID == -1) {
      return false;
    }
    return Hashing.unhashPassword(users.get(uniqueID).getPassword(), password) ? true : false;
  }

  /**
   * Change the username of a user.
   * 
   * @param id          unique id
   * @param newUsername requested new username
   * @return null if successfull, otherwise error description
   */
  public String changeUsername(int id, String newUsername) {
    User userToModify = getUserByID(id);
    if (userToModify == null) {
      return "User with the id '" + id + "does not exist";
    }

    if (!isUnique(newUsername)) {
      return "'" + newUsername + "' is not unique";
    }

    userToModify.setName(newUsername);
    return null;
  }

  /**
   * Change the password of a user.
   * 
   * @param id          unique id
   * @param newPassword requested new password
   * @return null if successfull, otherwise error description
   */
  public String changePassword(int id, String newPassword) {
    User userToModify = getUserByID(id);
    if (userToModify == null) {
      return "User with the id '" + id + "does not exist";
    }

    String[] passwordHash = Hashing.hashPassword(newPassword);
    if (passwordHash[0] == null) {
      return passwordHash[1];
    }
    userToModify.setPassword(passwordHash[0]);
    return null;
  }

  /**
   * Change the email of a user.
   * 
   * @param id      unique id
   * @param newMail requested new password
   * @return null if successfull, otherwise error description
   */
  public String changeMail(int id, String newMail) {
    User userToModify = getUserByID(id);
    if (userToModify == null) {
      return "User with the id '" + id + "does not exist";
    }
    if (!isValidEmailAddress(newMail)) {
      return "'" + newMail + "' is not a valid email!";
    }
    userToModify.setMail(newMail);
    return null;
  }

  public static boolean isValidEmailAddress(String email) {
    boolean result = true;
    try {
      InternetAddress mail = new InternetAddress(email);
      mail.validate();
    } catch (AddressException e) {
      result = false;
    }
    return result;
  }

}