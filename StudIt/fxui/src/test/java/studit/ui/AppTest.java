package studit.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class AppTest extends ApplicationTest {

  private AppController appController;
  private CourseController courseController;
  private ObservableList<String> list = FXCollections.observableArrayList();
  private ListView<String> listView;
  String a = "TDT4109";
  String b = "TMA4145";
  String c = "TTM4175";
  String d = "IT1901";


  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader appLoader = new FXMLLoader(getClass().getResource("App.fxml"));
    final FXMLLoader courseLoader = new FXMLLoader(getClass().getResource("App.fxml"));

    final Parent root = appLoader.load();
    this.appController = appLoader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }




  @BeforeEach
  public void setup() throws Exception {
    list.addAll(a,b,c,d);
    listView.setItems(list);
  }

  @Test
  public void hasChatBotButton() {
    BorderPane rootNode = (BorderPane) appController.rootPane.getScene().getRoot();
    Button button = from(rootNode).lookup(".button").query();
    assertEquals("Chatbot", button.getText());
  }

  @Test
  public void hasMainPageButton() {
    BorderPane rootNode = (BorderPane) appController.rootPane.getScene().getRoot();
    Button button = from(rootNode).lookup(".button").query();
    assertEquals("Hjem", button.getText());
  }

  @Test
  public void hasLogoutButton() {
    BorderPane rootNode = (BorderPane) appController.rootPane.getScene().getRoot();
    Button button = from(rootNode).lookup(".button").query();
    assertEquals("Logg ut", button.getText());
  }

  @Test
  public void hasNtnuButton() {
    BorderPane rootNode = (BorderPane) appController.rootPane.getScene().getRoot();
    Button button = from(rootNode).lookup(".button").query();
    assertEquals("NTNU", button.getText());
  }

  @Test
  public void testLogoutAction() {
    Button button = (Button) lookup("#logout_btn");
    clickOn(button);
    FXAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
  }

  @Test
  public void testOpenChatBot() {
    Button button = (Button) lookup("#chatbot_btn");
    clickOn(button);
    FxAssert.verifyThat(window("Chatbot"), WindowMatchers.isShowing());
  }


  @SuppressWarnings("unchecked")
  @Test
  public void testClickOnCourse() {
    ListView<String> listView = (ListView<String>) lookup("#coursesList");
    clickOn(listView.getItems().get(2));
    listView.getSelectionModel().getSelectedItem();

  }

  @SuppressWarnings("#unchecked")
  @Test
  public void hasExactlyNumItems() {
    ListView<String> listView = (ListView<String>) lookup("#coursesList");
    int numberOfItems = listView.getItems().size();
    assertEquals(numberOfItems, 4);
  }


  @Test
  public void comment(FxRobot robot){
  String comment = "Jeg synes dette er et kjedelig fag";
  TextField comment1 = (TextField) lookup("#comment1");
  Button button = (Button) lookup("#comment_btn");
  robot.clickOn(button);
  courseController.handleAddCommentAction(button.onclick());
  Assertions.verifyThat(comment1.hasText("Jeg synes dette er et kjedelig fag"));
  }

  @Test
  public void testClickOnMainPage(FxRobot robot) {
    Button button = (Button) lookup("#mainPage_btn");
    robot.clickOn(button);
  }
}
