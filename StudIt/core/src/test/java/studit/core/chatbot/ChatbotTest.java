package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatbotTest {
  
  private Chatbot chatbot;

  @BeforeEach
  public void init() {
    chatbot = new Chatbot();
  }
  
  @Test
  public void testDisplayWindow() {
    
  }

  @Test
  public void testManageInput() {
    assertEquals(chatbot.manageInput("Hei!").response, "Hei! ");
  }

}
