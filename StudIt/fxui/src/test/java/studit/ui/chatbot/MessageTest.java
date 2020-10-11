package studit.ui.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import studit.core.chatbot.Response;

public class MessageTest {

  private Message chatbotMessage;
  private Message userMessage;
  private Message responseMessage;

  @BeforeEach
  public void init() {
    chatbotMessage = new Message("", "chatbot");
    userMessage = new Message("", "user");
    
    Response response = new Response();
    response.add("one two three four five six seven eight nine");
    responseMessage = new Message(response, "chatbot");
  }

  @Test
  public void testGetText() {
    String testMsg = "test-message";
    String expectedMsg = "test-message ";
    chatbotMessage.setText(testMsg);
    userMessage.setText(testMsg);

    assertEquals(chatbotMessage.getText(), expectedMsg);
    assertEquals(userMessage.getText(), expectedMsg);
    assertEquals("one two three four five six seven eight \nnine ", responseMessage.getText());
  }


  @Test
  public void testGetUser() {
    assertEquals(chatbotMessage.getUser(), "chatbot");
    assertEquals(userMessage.getUser(), "user");
  }
  
  @Test
  public void testClick() {
    chatbotMessage.click();
    assertTrue(chatbotMessage.isClicked());
  }
  
  @Test
  public void testGetPrompt() {
    chatbotMessage.setPromt(List.of(new String[] {"2", "3"}, new String[] {}));
    List<String[]> result = chatbotMessage.getPrompt();
    assertEquals("2", result.get(0)[0]);
    assertEquals("3", result.get(0)[1]);
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      @SuppressWarnings("unused")
      String a = result.get(0)[2];
    });
  }

}
