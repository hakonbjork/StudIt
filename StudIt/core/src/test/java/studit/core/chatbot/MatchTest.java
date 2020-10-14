package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MatchTest {

  @Test
  public void matchTest() {
    Match match = new Match("årekar", 0.2f, 1, "[a-z]");
    
    assertEquals(1, match.precedence);
    assertEquals("årekar", match.command);
    assertEquals(0.2f, match.match);
    
    assertEquals("Match [match=0.2, command=årekar, precedence=1, dataMatch=[a-z]]", match.toString());
  }

}
