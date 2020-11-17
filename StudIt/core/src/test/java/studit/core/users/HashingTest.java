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
    assertEquals("Passordet må være mellom 8 og 32 tegn", pass1[1]);

    String pass2[] = hashPassword(longPassword);
    assertEquals("Passordet må være mellom 8 og 32 tegn", pass2[1]);

    String pass3[] = hashPassword(spacePassword);
    assertEquals("Passordet kan ikke inneholde mellomrom", pass3[1]);

    String pass4[] = hashPassword(illegalChar);
    assertEquals("Passordet har de følgende ulovlige tegnene: '" + "|" + "'", pass4[1]);
  }

}