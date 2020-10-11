package studit.core.chatbot.prompt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PromptManagerTest {

  @Test
  void testHandlePrompt() {
    ActionRequest action = new ActionRequest();
    PromptManager pm = new PromptManager();
    action.setFuncKey("exit");
    action.setChatbotResponse("Avslutter chatbot...");
    
    assertTrue("exit" == pm.handlePrompt("exit").getFuncKey());
    assertTrue("Avslutter chatbot..." == pm.handlePrompt("exit").getChatbotResponse());
  }

}
