package studit.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseTest extends ApplicationTest {

  private CourseController courseController;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader courseLoader = new FXMLLoader(getClass().getResource("Course.fxml"));
    final Parent root = courseLoader.load();
    this.courseController = courseLoader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testCourseController() {
    assertNotNull(this.courseController);
  }

  @Test
  public void testLogoutAction() {
    clickOn("#logout_btn");
    FxAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
  }

  @Test
  public void testOpenChatBot() {
    clickOn("#chatbot_btn");
    FxAssert.verifyThat(window("Chatbot"), WindowMatchers.isShowing());
  }

  // @Test
  // public void testClickOnMainPage() {
  // clickOn("#mainPage_btn");
  // FxAssert.verifyThat(window("StudIt"), WindowMatchers.isShowing());
  // }

  @Test
  public void testComment() {
    String comment = "Jeg synes dette er et kjedelig fag";
    courseController.commentField.setText(comment);
    clickOn("#addComment_btn");
    Assertions.assertThat((courseController.comment1.getText()).equals("Jeg synes dette er et kjedelig fag"));
  }

  @Test
  public void testSecondComment() {
    String comment1 = "Jeg synes dette er et kjedelig fag";
    courseController.commentField.setText(comment1);
    clickOn("#addComment_btn");
    String comment2 = "Jeg synes dette er et gøy fag";
    courseController.commentField.setText(comment2);
    clickOn("#addComment_btn");
    Assertions.assertThat((courseController.comment2.getText()).equals("Jeg synes dette er et gøy fag"));
  }

  @Test
  public void testThirdComment() {
    String comment1 = "Jeg synes dette er et kjedelig fag";
    courseController.commentField.setText(comment1);
    clickOn("#addComment_btn");
    String comment2 = "Jeg synes dette er et gøy fag";
    courseController.commentField.setText(comment2);
    clickOn("#addComment_btn");
    String comment3 = "Her bør du lese mye gjennom semesteret";
    courseController.commentField.setText(comment3);
    clickOn("#addComment_btn");
    Assertions.assertThat((courseController.comment2.getText()).equals("Her bør du lese mye gjennom semesteret"));
  }
}
