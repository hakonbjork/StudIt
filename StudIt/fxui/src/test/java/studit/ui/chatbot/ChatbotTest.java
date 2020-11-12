package studit.ui.chatbot;

import static org.testfx.api.FxAssert.verifyThat;

import javafx.stage.Stage;
import studit.ui.remote.DirectStuditModelAccess;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;


public class ChatbotTest extends ApplicationTest {

  private Chatbot chatbot;

  @Override
  public void start(Stage stage) {
    chatbot = new Chatbot(new DirectStuditModelAccess());
    chatbot.show();
  }

  @Test
  public void testChatbot() {
    verifyThat(window("Chatbot"), WindowMatchers.isShowing());
    clickOn("#btn_minimize");
    verifyThat(window("Chatbot"), WindowMatchers.isNotFocused());
  }

}