package studit.core.chatbot.prompt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ResponseManagerTest {

  @Test
  void testHandlePrompt() {
    ActionRequest action = new ActionRequest();
    ResponseManager pm = new ResponseManager();
    action.setFuncKey("exit");
    action.setChatbotResponse("Avslutter chatbot...");
    
    assertTrue("exit" == pm.handlePrompt("exit").getFuncKey());
    assertTrue("Avslutter chatbot..." == pm.handlePrompt("exit").getChatbotResponse());
  }

}
