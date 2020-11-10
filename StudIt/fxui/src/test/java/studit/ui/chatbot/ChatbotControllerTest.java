package studit.ui.chatbot;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.util.WaitForAsyncUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;

public class ChatbotControllerTest extends ApplicationTest {

  private ChatbotController chatbotController;
  private final RemoteStuditModelAccess remote = new DirectStuditModelAccess();

  /*
   * @Override public void init() throws Exception { FxToolkit.registerStage(() ->
   * new Stage()); }
   * 
   * @Override public void stop() throws Exception { super.stop();
   * FxToolkit.hideStage(); }
   */

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/studit/ui/Chatbot.fxml"));
    final Parent root = loader.load();
    this.chatbotController = (ChatbotController) loader.getController();
    chatbotController.setRemoteAccess(this.remote);
    chatbotController.setStage(stage);
    stage.setScene(new Scene(root));
    stage.setTitle("chatbot");
    stage.show();
    stage.toFront();
  }

  @Test
  public void testChatbot() {
    FxRobot robot = new FxRobot();
    assertNotNull(window("chatbot"));
    robot.clickOn("#txt_user_entry").write("hei", 10).press(KeyCode.ENTER);
    robot.press(KeyCode.ENTER);

    assertEquals("Hei! Jeg er din nye assistent, chatbotten Gunnar. Hva kan jeg hjelpe deg med? ",
        this.chatbotController.list_chat.getItems().get(0).getText().replaceAll("\n", ""));

    assertEquals("hei ", this.chatbotController.list_chat.getItems().get(1).getText().replaceAll("\n", ""));

  }

  /*
  @Test
  public void testLoginController() {
    assertNotNull(this.chatbotController);
  } 

  /*
  @Test
  public void testMinimize() {
    clickOn("#btn_minimize");
    verifyThat(window("chatbot"), WindowMatchers.isNotFocused());
    clickOn("#btn_minimize");
  }
  
  @Test
  public void testCleanExit() {
    FxRobot robot = new FxRobot();
    robot.clickOn("#btn_exit");
    assertNull(this.chatbotController.getStage());
  }

  @Test
  public void testToolbarCommands() {
    assertNotNull(window("chatbot"));

    // Note the position before moving the toolbar
    double xBefore = window("chatbot").getX();
    double yBefore = window("chatbot").getY();

    // Drag the window by the toolbar
    FxRobot robot = new FxRobot();
    robot.moveTo("#toolbar_main").drag(MouseButton.PRIMARY).dropBy(100, 100);

    // Verify that the position of the window changed according to the drag and
    // drop.
    assertEquals(100, window("chatbot").getX() - xBefore);
    assertEquals(100, window("chatbot").getY() - yBefore);

    double xBefore2 = window("chatbot").getX();
    double yBefore2 = window("chatbot").getY();

    robot.moveTo("#toolbar_main").drag(MouseButton.PRIMARY).dropBy(100, 100);
    assertEquals(100, window("chatbot").getX() - xBefore2);
    assertEquals(100, window("chatbot").getY() - yBefore2);

    clickOn("#btn_exit");
    // Check that the stage has been succesfully terminated
    assertThrows(NoSuchElementException.class, () -> window("chatbot"));
  } */

}