package studit.core.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static studit.core.users.Hashing.*;

public class HashingTest {
  
  @Test
  public void testHashing() {
    String hash = "hfjekrjveofeuf648tu3!RU!1jfkf..wdjwfj";
    String[] hashedPassword = hashPassword("TestPassword");
    boolean bol = unhashPassword(hash, hashedPassword[0]);
    assertFalse(bol);
  }

  @Test
  public void testIllegalHashing(){
    String shortPassword = "pass";
    String longPassword = "thisPasswordIsTooLongTooManyCharacters";
    String spacePassword = "pass word";
    String illegalChar = "|password";
    
    String pass1[] = hashPassword(shortPassword);
    assertEquals("Password must be between 8 and 32 characters", pass1[1]);

    String pass2[] = hashPassword(longPassword);
    assertEquals("Password must be between 8 and 32 characters", pass2[1]);

    String pass3[] = hashPassword(spacePassword);
    assertEquals("Password cannot contain spaces", pass3[1]);

    String pass4[] = hashPassword(illegalChar);
    assertEquals("Your password contains the following illegal chars: '" + "|" + "'", pass4[1]);
  }

}