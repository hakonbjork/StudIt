package studit.core.users;

public class User {

  private String name;
  private String username;
  private String mail;
  private String password;
  private int uniqueID;

  public User() {

  }

  /**
   * Creates a new user with the following parameters.
   * 
   * @param name     - The name of the user.
   * @param username - The username.
   * @param mail     - The mail of the user.
   * @param password - The user's password.
   * @param uniqueID - An uniqueID for every user, can be used to identify the
   *                 user .
   */
  public User(String name, String username, String mail, String password, int uniqueID) {
    this.name = name;
    this.username = username;
    this.mail = mail;
    this.password = password;
    this.uniqueID = uniqueID;
  }

  /**
   * Gets the user's name.
   * 
   * @return - The user's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the user's name.
   * 
   * @param name - The name to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the username of the user.
   * 
   * @return The user's username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the user's username.
   * 
   * @param username - The username to be set.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the user's mail.
   * 
   * @return - The user's mail.
   */
  public String getMail() {
    return mail;
  }

  /**
   * Sets the user's mail.
   * 
   * @param mail - The mail to be set.
   */
  public void setMail(String mail) {
    this.mail = mail;
  }

  /**
   * Gets the user's password.
   * 
   * @return The user's password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's password.
   * 
   * @param password - The password to be set.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * A way to represent user objects in an understandable way.
   * 
   * @return - A String with the users username.
   */
  @Override
  public String toString() {
    return "User{" + "username='" + username + '\'' + '}';
  }

  /**
   * Gets the user's uniqueID.
   * 
   * @return The user's uniqueID.
   */
  public int getUniqueID() {
    return uniqueID;
  }

  /**
   * Sets the user's uniqueID.
   * 
   * @param uniqueID - The uniqueID to be set.
   */
  public void setUniqueID(int uniqueID) {
    this.uniqueID = uniqueID;
  }
}
