package studit.core.chatbot.prompt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ResponseManagerTest {

  @Test
  public void testHandlePrompt() {
    ActionRequest action = new ActionRequest();
    ResponseManager pm = new ResponseManager();
    action.setFuncKey("exit");
    action.setChatbotResponse("Avslutter chatbot...");
    
    assertEquals("exit", pm.handlePrompt("exit").getFuncKey());
    assertEquals("Avslutter chatbot...", pm.handlePrompt("exit").getChatbotResponse());

    assertNull(pm.handlePrompt("regret").getFuncKey());
    assertEquals("Den er god du!", pm.handlePrompt("regret").getChatbotResponse());

    assertEquals("foo", pm.handlePrompt("foo").getFuncKey());
    assertEquals("", pm.handlePrompt("foo").getChatbotResponse());
  }


}
