package studit.ui.chatbot;

import static org.testfx.api.FxAssert.verifyThat;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import studit.ui.remote.RemoteStuditModelAccess;

public class ChatbotRemoteTest extends ApplicationTest {
 
  private Chatbot chatbot;

  @Override
  public void start(Stage stage) {
    chatbot = new Chatbot(new RemoteStuditModelAccess());
    chatbot.show();
  }

  @Test
  public void testChatbot() {
    verifyThat(window("Chatbot"), WindowMatchers.isShowing());
    clickOn("#btn_minimize");
    verifyThat(window("Chatbot"), WindowMatchers.isNotFocused());
  }
}