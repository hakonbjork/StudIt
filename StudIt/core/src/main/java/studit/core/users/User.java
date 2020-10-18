package studit.core.users;

public class User {

  private String name;
  private String username;
  private String mail;
  private String password;
  private int uniqueID;

  public User() {

  }

  // TODO: This method is decrepated, and must be removed in future releases
  public User(String name, String username, String mail, String password) {
    this.name = name;
    this.username = username;
    this.mail = mail;
    this.password = password;
    this.uniqueID = 0;
  }

  public User(String name, String username, String mail, String password, int uniqueID) {
    this.name = name;
    this.username = username;
    this.mail = mail;
    this.password = password;
    this.uniqueID = uniqueID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" + "username='" + username + '\'' + '}';
  }

  public int getUniqueID() {
    return uniqueID;
  }

  public void setUniqueID(int uniqueID) {
    this.uniqueID = uniqueID;
  }
}
