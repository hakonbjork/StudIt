package studit.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
// import studit.ui.remote.DirectStuditModelAccess;
// import studit.ui.remote.RemoteStuditModelAccess;

public class CourseControllerTest extends ApplicationTest {

  private CourseController courseController;
  // private final RemoteStuditModelAccess remote = new DirectStuditModelAccess();

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
    assertEquals("mamma", courseController.getHjelpemidler());
  }

  @Test
  public void testSetTips(){
    courseController.setTips("kjøp boka");
    assertEquals("kjøp boka", courseController.getTipsTricks());
  }

  @Test
  public void testSetLitteratur(){
    courseController.setLitterature("Kompendie");
    assertEquals("Kompendie", courseController.getLitterature());
  }

  @Test
  public void testSetCourseInformation(){
    courseController.setCourseInformation("bra fag");
    assertEquals("bra fag", courseController.getCourseInformation());
  }

  @Test
  public void testSetVurderingsform(){
    courseController.setVurderingsForm("Eksamen");
    assertEquals("Eksamen", courseController.getVurderingsform());
  }

  @Test
  public void testSetRating(){
    courseController.setRating(2.3);
    assertEquals(2.3, courseController.getRating());
  }

  @Test
  public void testSetEksamensdato(){
    courseController.setEksamensdato("30.10.2020");
    assertEquals("30.10.2020", courseController.getEksamensdato());
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

  // @Test
  // public void testClickOnDiscussion() {
  //   clickOn("#discussion_btn");
  //   FxAssert.verifyThat(window("Discussion"), WindowMatchers.isShowing());
  // }
}