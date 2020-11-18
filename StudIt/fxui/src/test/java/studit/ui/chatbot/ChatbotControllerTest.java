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
import javafx.scene.Node;
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
    robot.clickOn("#txt_user_entry").write("hei", 2);
    robot.push(KeyCode.ENTER);

    assertEquals("Hei! Jeg er din nye assistent, chatbotten Gunnar. Hva kan jeg hjelpe deg med? ",
        this.chatbotController.list_chat.getItems().get(0).getText().replaceAll("\n", ""));

    assertEquals("hei ", this.chatbotController.list_chat.getItems().get(1).getText());
    assertEquals("Hei! ", this.chatbotController.list_chat.getItems().get(2).getText());

    robot.write("kan du fortelle litt om", 2);
    robot.push(KeyCode.ENTER);
    assertEquals(
        "Jeg forstår ikke hvilken informasjon du etterspør, husk å spesifisere hvilket fag du vil vite mer om. ",
        this.chatbotController.list_chat.getItems().get(4).getText().replaceAll("\n", ""));

    robot.write("kan jeg få se fagoversikten?", 2);
    robot.push(KeyCode.ENTER);
    assertTrue(this.chatbotController.list_chat.getItems().get(4).getText().contains("\n"));

  }

  @Test
  public void testChatbotController() {
    assertNotNull(this.chatbotController);
    assertNotNull(chatbotController.getStage());
    chatbotController.setRemoteAccess(new RemoteStuditModelAccess());

    FxRobot robot = new FxRobot();
    robot.clickOn("#txt_user_entry").write("jeg vil se fagoversikten", 5);
    robot.push(KeyCode.ENTER);

    assertEquals("Error -> could not establish connection to server ",
        this.chatbotController.list_chat.getItems().get(2).getText().replaceAll("\n", ""));
  }

  @Test
  public void testPrompt() throws ApiCallException {
    chatbotController.setCommands(new Commands(chatbotController, remote));
    FxRobot robot = new FxRobot();
    robot.clickOn("#txt_user_entry").write("hva er anbefalt lesestoff i TMA4130?", 2);
    robot.push(KeyCode.ENTER);

    Node node = robot.lookup(".hyperlink").nth(0).query();
    robot.clickOn(node);
    assertEquals(
        "Her har du våre anbefalinger i " + "TMA4140" + ": "
            + remote.getCourseByFagkode("TMA4140").getAnbefaltLitteratur() + " ",
        chatbotController.list_chat.getItems().get(3).getText().replaceAll("\n", ""));

    Node node2 = robot.lookup(".hyperlink").nth(1).query();
    robot.clickOn(node2);
    assertTrue(chatbotController.list_chat.getItems().size() == 4);

    robot.clickOn("#txt_user_entry").write("hva er anbefalt lesestoff i TMA4130?", 2);
    robot.push(KeyCode.ENTER);

    Node node3 = robot.lookup(".hyperlink").nth(1).query();
    robot.clickOn(node3);

    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ",
        chatbotController.list_chat.getItems().get(6).getText().replaceAll("\n", ""));

    Node node4 = robot.lookup(".hyperlink").nth(2).query();
    robot.clickOn(node4);
    assertTrue(chatbotController.list_chat.getItems().size() == 7);
  }

  @Test
  public void testExitByPrompt() {
    FxRobot robot = new FxRobot();
    robot.clickOn("#txt_user_entry").write("Jeg vil avslutte", 2);
    robot.push(KeyCode.ENTER);
    Node node = robot.lookup(".hyperlink").nth(0).query();
    clickOn(node);
    assertThrows(NoSuchElementException.class, () -> window("chatbot"));
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