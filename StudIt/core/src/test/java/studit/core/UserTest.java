package studit.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
  private User user;

  @BeforeEach
  public void initEach() {
    this.user = new User("Hans Hansen", "hanse", "hanse@gmail.com", "hanse123");
  }

  @Test
  public void testGetName() {
    assertEquals(user.getName(), "Hans Hansen");
  }

  @Test
  public void testSetUsername() {
    user.setUsername("hansen");
    assertEquals("hansen", user.getUsername());
  }

  @Test
  public void testGetMail() {
    assertEquals("hanse@gmail.com", user.getMail());
  }

  @Test
  public void testToString() {
    String expected = "User{username='hanse'}";
    assertEquals(expected, user.toString());
  }
}