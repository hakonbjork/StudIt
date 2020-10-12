package studit.core.chatbot.prompt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActionRequestTest {
  
  private ActionRequest action;
  
  @BeforeEach
  public void init() {
    action = new ActionRequest();
  }
  
  @Test
  public void testAddArgument() {
    action.addArgument(0);
    action.addArgument("foo");
    action.addArgument(true);
    
    assertEquals(List.of(0, "foo", true), action.getArguments());
  }
  
  @Test
  public void testGetChatbotResponse() {
    action.setChatbotResponse("foo");
    assertEquals("foo", action.getChatbotResponse());
  }
  
  @Test
  public void testGetFuncKey() {
    action.setFuncKey("foo");
    assertEquals("foo", action.getFuncKey());
  }
  
  @Test
  public void testGetArguments() {
    action.setArguments(List.of(1, "foo", true));
    assertEquals(List.of(1, "foo", true), action.getArguments());
  }

}
