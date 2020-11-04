package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatbotManagerTest {

  private ChatbotManager chatbotManager;

  @BeforeEach
  public void init() {
    chatbotManager = new ChatbotManager(List.of(new String[] {"foo", "foo2"}, new String[] {"foo2", "foo3"}));
  }

  @Test
  public void testManageInput() {
    assertEquals(chatbotManager.manageInput("Hei!").getResponse(), "Hei! ");
  }

}
