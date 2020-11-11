package studit.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;

public class CourseControllerTest extends ApplicationTest {

  private CourseController courseController;
  private final RemoteStuditModelAccess remote = new DirectStuditModelAccess();

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
  public void testSetHjelpemidler(){
    courseController.setHjelpemidler("mamma");
    TextField hjelpemidler = (TextField) lookup("#hjelpemidler");
    assertEquals("mamma", hjelpemidler.getText());
  }

  @Test
  public void testSetTips(){
    courseController.setTips("kjøp boka");
    TextField tips = (TextField) lookup("#tips");
    assertEquals("kjøp boka", tips.getText());
  }

  @Test
  public void testSetRating(){
    courseController.setRating(2.3);
    assertEquals(2.3, courseController.getRating());
  }

  @Test
  public void testLogoutAction() {
    clickOn("#logout_btn");
    FxAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
  }

   @Test
  public void hasLogoutButton() {
    BorderPane rootNode = (BorderPane)
    courseController.rootPane.getScene().getRoot();
    Button button = from(rootNode).lookup("Logg ut").query();
    assertEquals("Logg ut", button.getText());
  } 

  @Test
  public void testClickOnDiscussion() {
    clickOn("#openDiscussion");
    FxAssert.verifyThat(window("Discussion"), WindowMatchers.isShowing());
  }
  
}