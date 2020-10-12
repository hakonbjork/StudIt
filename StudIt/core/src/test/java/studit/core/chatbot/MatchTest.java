package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MatchTest {

  @Test
  public void matchTest() {
    Match match = new Match("åre2kar123", 0.2f, 1);
    
    assertEquals(1, match.precedence);
    assertEquals("årekar", match.command);
    assertEquals(0.2f, match.match);
    
    assertEquals("Match [match=0.2, command=årekar, precedence=1]", match.toString());
  }

}
