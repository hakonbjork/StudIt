package studit.ui.chatbot;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import studit.ui.AppController;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;

public class ChatbotControllerTest extends ApplicationTest {

  private ChatbotController chatbotController;
  private final RemoteStuditModelAccess remote = new DirectStuditModelAccess();

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

  @BeforeEach
  public void setUp() throws ApiCallException {
    AppController.newChatbot(true);
  }

  @Test
  public void testChatbot() {
    FxRobot robot = new FxRobot();
    robot.clickOn("#txt_user_entry").write("hei", 10);
    robot.push(KeyCode.ENTER);

    assertEquals("Hei! Jeg er din nye assistent, chatbotten Gunnar. Hva kan jeg hjelpe deg med? ",
        this.chatbotController.list_chat.getItems().get(0).getText().replaceAll("\n", ""));

    assertEquals("hei ", this.chatbotController.list_chat.getItems().get(1).getText());
    assertEquals("Hei! ", this.chatbotController.list_chat.getItems().get(2).getText());

    robot.write("kan du fortelle litt om", 10);
    robot.push(KeyCode.ENTER);
    assertEquals(
        "Jeg forstår ikke hvilken informasjon du etterspør, husk å spesifisere hvilket fag du vil vite mer om. ",
        this.chatbotController.list_chat.getItems().get(4).getText().replaceAll("\n", ""));

    robot.write("kan jeg få se fagoversikten?", 10);
    robot.push(KeyCode.ENTER);
    assertTrue(this.chatbotController.list_chat.getItems().get(4).getText().contains("\n"));
    
  }

  @Test
  public void testLoginController() {
    assertNotNull(this.chatbotController);
  }

  @Test
  public void testMinimize() {
    clickOn("#btn_minimize");
    verifyThat(window("chatbot"), WindowMatchers.isNotFocused());
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

    clickOn("#btn_exit");
    // Check that the stage has been succesfully terminated
    assertThrows(NoSuchElementException.class, () -> window("chatbot"));
  }

}