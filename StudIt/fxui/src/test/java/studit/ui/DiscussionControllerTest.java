package studit.ui;

import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import studit.core.mainpage.CourseItem;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;

public class DiscussionControllerTest extends ApplicationTest {

  private DiscussionController discussionController;

  private CourseItem item;

  private DirectStuditModelAccess directStuditModelAccess = new DirectStuditModelAccess();

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader discussionLoader = new FXMLLoader(getClass().getResource("Discussion.fxml"));
    final Parent root = discussionLoader.load();
    this.discussionController = discussionLoader.getController();

    item = this.directStuditModelAccess.getCourseByFagkode("TDT4120");
    discussionController.addCourse(item);

    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testDiscussionController() {
    assertNotNull(this.discussionController);
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

  @Test
  public void testClickOnMainPage() {
    clickOn("#mainPage_btn");
    FxAssert.verifyThat(window("StudIt"), WindowMatchers.isShowing());
  }

  @Test
  public void testComments() {
          
      String comment = "Jeg synes dette er et kjedelig fag";
      discussionController.getInputField().setText(comment);
      clickOn("#information_btn");


      String comment2 = "Uenig, det er et kult fag";
      discussionController.getInputField().setText(comment2);
      clickOn("#information_btn");

      //Assertions.assertThat((discussionController.getForumList().getItems().get(1).getKommentar()).equals("Uenig, det er et kult fag"));

    
  }

  
}