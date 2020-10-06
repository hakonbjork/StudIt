package studit.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserManagerTest {

  @BeforeEach
  public void initEach() {
    UserManager.startStuff();
  }

  @Test
  public void testAddUsers() {
    assertTrue(UserManager.containsUser("user", "password"));
    assertFalse(UserManager.containsUser("abc", "def"));
  }

  @Test
  public void testAddExistingUser() {
    User newUser = new User("John", "user", "j@m.c", "john123");
    boolean b = UserManager.addUser(newUser);
    assertFalse(b);
  }


}