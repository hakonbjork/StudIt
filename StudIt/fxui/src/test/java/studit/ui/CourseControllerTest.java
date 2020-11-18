package studit.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import static org.testfx.api.FxAssert.verifyThat;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;
import studit.core.mainpage.CourseItem;
import studit.core.users.User;

public class CourseControllerTest extends ApplicationTest {

  private CourseController courseController;
  private final RemoteStuditModelAccess remoteStuditModelAccess = new DirectStuditModelAccess();

  @Override
  public void start(final Stage stage) throws Exception {
    LoginController.setTestingMode(true);
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("Course.fxml"));
    final Parent root = loader.load();
    this.courseController = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testAppController() {
    assertNotNull(this.courseController);
  }

  @Test
  public void testSetHjelpemidler() {
    courseController.setHjelpemidler("mamma");
    assertEquals("mamma", courseController.getHjelpemidler());
  }

  @Test
  public void testSetTips() {
    courseController.setTips("kjøp boka");
    assertEquals("kjøp boka", courseController.getTipsTricks());
  }

  @Test
  public void testSetLitteratur() {
    courseController.setLitterature("Kompendie");
    assertEquals("Kompendie", courseController.getLitterature());
  }

  @Test
  public void testSetCourseInformation() {
    courseController.setCourseInformation("bra fag");
    assertEquals("bra fag", courseController.getCourseInformation());
  }

  @Test
  public void testSetVurderingsform() {
    courseController.setVurderingsForm("Eksamen");
    assertEquals("Eksamen", courseController.getVurderingsform());
  }

   @Test
  public void testSetRating() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        courseController.setRating(1.0);
        assertEquals(1.0, courseController.getRating());
      }
    });
  }

  @Test
  public void testSetEksamensdato() {
    courseController.setEksamensdato("30.10.2020");
    assertEquals("30.10.2020", courseController.getEksamensdato());
  }

  @Test
  public void testLogoutAction() {
    clickOn("#logout_btn");
    FxAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
  }

  @Test
  public void testMainPageAction() {
    clickOn("#mainpage_back");
    FxAssert.verifyThat(window("App"), WindowMatchers.isShowing());
  }

  @Test
  public void testMainPageAction2() {
    clickOn("#mainPage_btn");
    FxAssert.verifyThat(window("App"), WindowMatchers.isShowing());
  }

  @Test
  public void hasLogoutButton() {
    BorderPane rootNode = (BorderPane) courseController.rootPane.getScene().getRoot();
    Button button = from(rootNode).lookup("Logg ut").query();
    assertEquals("Logg ut", button.getText());
  }

  @Test
  public void testSetCurrentUser() throws ApiCallException {
    User berte = remoteStuditModelAccess.getUserByUsername("Berte92");
    courseController.setCurrentUser(berte);
    assertEquals(berte, courseController.getCurrentUser());
  }

  @Test
  public void testSetFagkode() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        String fagkode = "IT1901";
        courseController.setFagkode(fagkode);
        assertEquals("IT1901", courseController.getFagkode());
      }
    });
  }

  @Test
  public void testSetFagNavn() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        String fagnavn = "Informatikk";
        courseController.setFagnavn(fagnavn);
        assertEquals("Informatikk", courseController.getFagnavn());
      }
    });
  }

  // @Test
  // public void testOpenChatbot(){
  //   clickOn("#chatbot_btn");
  // }

  @Test
  public void testClickOnDiscussion() throws ApiCallException {
    CourseItem courseItem = remoteStuditModelAccess.getCourseByFagkode("IT1909");
    User berte = remoteStuditModelAccess.getUserByUsername("Berte92");
    courseController.setCurrentUser(berte);
    courseController.setCourseItem(courseItem);
    clickOn("#discussion_btn");
  }
}