package studit.core.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UsersTest {

  private Users u;
  private String userNotExist;

  public UsersTest() {
    this.u = new Users();
    this.userNotExist = "User with the id '" + 0 + "does not exist";
  }

  private void addRandomUser(){
    u.addUser("Bjarne", "bjarn", "bjarne@gmail.com", "Bjarne123");
  }

  @Test
  public void addUserFieldNull() {  
    String[] s = u.addUser(null, "username", "mail", "password");
    String[] t = u.addUser("name", null, "mail", "password");
    String[] v = u.addUser("name", "username", null, "password");
    String[] w = u.addUser("name", "username", "mail", null);

    String expected = "Manglende felt, forventet navn, brukernavn, mail, og passord";
    assertEquals(expected, s[1]);
    assertEquals(expected, t[1]);
    assertEquals(expected, v[1]);
    assertEquals(expected, w[1]);
  }

  @Test
  public void addUserShortPassword() {
    String[] s = u.addUser("Bjartulf", "bjartern", "bjort@bjart.no", "bjert");
    assertNull(s[0]);
  }

  @Test
  public void addUserFailEmail() {
    String[] s = u.addUser("name", "username", "mail", "password");
    assertEquals("-4", s[2]);
  }

  @Test
  public void userNotUnique() {
    addRandomUser();
    String[] t = u.addUser("Bjarne", "bjarn", "bjarne@gmail.com", "Bjarne123");
    assertEquals("-2", t[2]);
  }

  @Test
  public void testRemoveUser() {
    addRandomUser();
    u.removeUser(0);
    User user = u.getUserByID(0);
    assertNull(user);
  }

  @Test
  public void testChangeUsername() {
    String s = u.changeUsername(0, "sindre");
    String notExisting = "User with the id '" + 0 + "does not exist";
    assertEquals(notExisting, s);

    addRandomUser();
    String t = u.changeUsername(0, "bjarn");
    String notUnique = "'" + "bjarn" + "' is not unique";
    assertEquals(notUnique, t);

    u.changeUsername(0, "bjern");
    User user = u.getUserByID(0);
    String username = user.getUsername();
    assertEquals("bjern", username);
  }

  @Test
  public void testChangePassword() {
    String s = u.changePassword(0, "newPassword");
    assertEquals(userNotExist, s);

    addRandomUser();
    String t = u.changePassword(0, "short");
    String message = "Passordet må være mellom 8 og 32 tegn";
    assertEquals(message, t);

    String v = u.changePassword(0, "newPassword");
    assertNull(v);
  }

  @Test
  public void testChangeMail() {
    String s = u.changeMail(0, "mail@mail.no");
    assertEquals(userNotExist, s);

    addRandomUser();
    String v = u.changeMail(0, "mail");
    String notValidMail = "'" + "mail" + "' is not a valid email!";
    assertEquals(notValidMail, v);

    String w = u.changeMail(0, "bjarn@bjorn.bjern");
    assertNull(w);
  }

  @Test
  public void testAuthenticateLogin() {
    boolean b = u.authenticateLogin("bjerarn", "password");
    assertFalse(b);

    addRandomUser();
    boolean c = u.authenticateLogin("bjarn", "Bjarne123");
    assertTrue(c);
  }

  @Test
  public void testGettersSetters() {
    addRandomUser();

    User user = u.getUserByID(0);
    Map<Integer, User> users = u.getUsers();
    assertEquals(users.get(0), user);

    Map<Integer, User> users2 = new HashMap<>();
    users2.put(0, user);
    assertEquals(users.get(0), users2.get(0));

    u.setPrevAssignedID(2);
    assertEquals(2, u.getPrevAssignedID());
  }


  
}