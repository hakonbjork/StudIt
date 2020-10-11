package studit.ui.chatbot;

import static org.junit.jupiter.api.Assertions.fail;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;
import studit.core.chatbot.prompt.Func;

class CommandsTest {

  @Test
  void testCommands() {
    Commands commands = new Commands(null);
    for (Entry<String, Func> entry : commands.getCommands().entrySet()) {
      try {
        entry.getValue().execute(null);
      } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
        // Expected due to empty object list if the function has parameteres
      } catch (Exception e) {
        fail("Command '" + entry.getKey() + "' failed with exception: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

}
