package studit.ui.chatbot;

import java.util.HashMap;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import studit.core.chatbot.prompt.Func;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;

class CommandsTest extends ApplicationTest {

  private ChatbotController controller;
  private HashMap<String, Func> commands;
  private Chatbot remoteChatbot;

  @Override
  public void start(Stage stage) {
    remoteChatbot = new Chatbot();
    remoteChatbot.show();
    this.controller = remoteChatbot.getController();
  }

  /**
   * To prevent the chatbot from restarting over and over consuming time and
   * resources, we do all the testing in this clause.
   * 
   * @throws ApiCallException - Something went horribly wrong, unknown reason.
   */
  @Test
  public void testCommands() throws ApiCallException {
    Chatbot chatbot = new Chatbot(true);
    commands = new Commands(controller, new DirectStuditModelAccess()).getCommands();

  }

  @Test
  public void testExitChatbot() throws ApiCallException {
    FxRobot robot = new FxRobot();
    //robot.interact(() -> commands.get("exit").execute(null));

    //commands = new Commands(controller, new DirectStuditModelAccess()).getCommands();
    //AppController.newChatbot(true);
    //assertThrows(IllegalStateException.class, () -> commands.get("exit").execute(null));
    //assertNull(AppController.getChatbot());
  }

}
