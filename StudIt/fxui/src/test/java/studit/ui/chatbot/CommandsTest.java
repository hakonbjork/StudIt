package studit.ui.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;
import studit.core.chatbot.prompt.Func;
import studit.core.mainpage.CourseItem;
import studit.ui.AppController;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;

class CommandsTest extends ApplicationTest {

  private ChatbotController controller;
  private HashMap<String, Func> commands;
  private DirectStuditModelAccess remote = new DirectStuditModelAccess();

  private Chatbot remoteChatbot;
  private final String fagkode = "TMA4140";
  private CourseItem course;

  private int lastIdx = 0;

  @Override
  public void start(Stage stage) {
    remoteChatbot = new Chatbot(remote);
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
    commands = new Commands(controller, new DirectStuditModelAccess()).getCommands();
    controller.setDisplayContent(false);

    course = remote.getCourseByFagkode(fagkode);

    testAnbefalt();
    testEksamen();
    testEksamensdato();
    testFaginfo();
    testHjelpemidler();
    testTips();
    testPensum();
    testVurderingsform();
    testFagoversikt();

    testThrownExceptions();
  }

  private void testAnbefalt() throws ApiCallException {
    Func func = commands.get("anbefalt");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Her har du våre anbefalinger i " + fagkode + ": " + course.getAnbefaltLitteratur() + " ",
        getLastItem());
  }

  private void testEksamen() throws ApiCallException {
    controller.setDisplayContent(false);
    Func func = commands.get("eksamen");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Her har du eksamensinformasjon for " + fagkode + ": " + "eksamensdato: " + course.getEksamensdato()
        + ", vurderingsform: " + course.getVurderingsform() + ", tillatte hjelpemidler: " + course.getHjelpemidler()
        + " ", getLastItem());
  }

  private void testEksamensdato() throws ApiCallException {
    Func func = commands.get("eksamensdato");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Eksamen i " + fagkode + " blir " + course.getEksamensdato() + " ", getLastItem());
  }

  private void testFaginfo() throws ApiCallException {
    Func func = commands.get("faginfo");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Her kommer litt informasjon om " + fagkode + ": " + course.getInformasjon() + " ", getLastItem());
  }

  private void testHjelpemidler() throws ApiCallException {
    Func func = commands.get("hjelpemidler");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("På eksamen i " + fagkode + " blir hjelpemidler: " + course.getHjelpemidler() + " ", getLastItem());
  }

  private void testTips() throws ApiCallException {
    Func func = commands.get("tips");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Her har du noen tips i " + fagkode + ": " + course.getTips() + " ", getLastItem());
  }

  private void testPensum() throws ApiCallException {
    Func func = commands.get("pensum");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Dette er pensum i " + fagkode + ": " + course.getPensumlitteratur() + " ", getLastItem());
  }

  private void testVurderingsform() throws ApiCallException {
    Func func = commands.get("vurderingsform");
    executeFunc(func, List.of());
    assertEquals("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt. ", getLastItem());

    executeFunc(func, List.of(fagkode));
    assertEquals("Vurderingsformen i " + fagkode + " er:  " + course.getVurderingsform() + " ", getLastItem());
  }

  private void testFagoversikt() throws ApiCallException {
    Func func = commands.get("fagoversikt");
    executeFunc(func, List.of());
    assertTrue(getLastItem().startsWith("Fagoversikt: " + fagkode));
  }

  /**
   * Test all the functions that should handle ApiCallExceptions
   * 
   * @throws ApiCallException only thrown if testing failed completely.
   */
  private void testThrownExceptions() throws ApiCallException {
    RemoteStuditModelAccess mockRemote = mock(RemoteStuditModelAccess.class);
    doThrow(new ApiCallException("Expected error")).when(mockRemote).getCourseByFagkode(any());
    doThrow(new ApiCallException("Expected error")).when(mockRemote).getCourseList();

    String connectionFailedMsg = "Kunne ikke etablere tilkobling til serveren ";

    commands = new Commands(controller, mockRemote).getCommands();

    executeFunc(commands.get("anbefalt"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("eksamen"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("eksamensdato"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("faginfo"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("hjelpemidler"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("tips"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("pensum"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("vurderingsform"), List.of(fagkode));
    assertEquals(connectionFailedMsg, getLastItem());

    executeFunc(commands.get("fagoversikt"), List.of());
    assertEquals(connectionFailedMsg, getLastItem());
  }

  @Test
  public void testExitChatbot() throws ApiCallException {
    commands = new Commands(controller, new DirectStuditModelAccess()).getCommands();
    AppController.newChatbot(true);
    commands.get("exit").execute(null);
    assertNull(AppController.getChatbot());
  }

  /**
   * Execute a function with the given args and increment last IDX
   * 
   * @param func fucn object to execute.
   * @param args List of arguments, cannot be null.
   */
  private void executeFunc(Func func, List<Object> args) {
    try {
      func.execute(args);
      lastIdx++;
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

  }

  /**
   * Gets the last item in the chatbot message field, used for testing if the
   * message field was updated correctly after a function was executed.
   */
  private String getLastItem() {
    return controller.list_chat.getItems().get(lastIdx).getText().replace("\n", "");
  }

}
