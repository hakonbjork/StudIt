package studit.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javafx.scene.Node;
import org.assertj.core.api.Assertions;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.Comment;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;

public class DiscussionControllerTest extends ApplicationTest {

  private DiscussionController discussionController;

  private CourseItem item;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader discussionLoader = new FXMLLoader(getClass().getResource("Discussion.fxml"));
    final Parent root = discussionLoader.load();
    this.discussionController = discussionLoader.getController();

    DirectStuditModelAccess r = new DirectStuditModelAccess();
    discussionController.setStuditModelAccess(r);
    item = this.discussionController.getStuditModelAcces().getCourseByFagkode("TDT4120");
    discussionController.addCourse(item);

    stage.setScene(new Scene(root));
    stage.show();
  }

   @Test
  public void testDiscussionController() {
    assertNotNull(this.discussionController);
  }


  //@Test
  //public void testOpenInformationTab() {
  //  clickOn("#information_btn");
  //  FxAssert.verifyThat(window("Course"), WindowMatchers.isShowing());
  //}

  //@Test
  //public void testClickOnMainPage() {
  //  clickOn("#mainPage_btn");
  //  FxAssert.verifyThat(window("StudIt"), WindowMatchers.isShowing());
  //}

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
  public void testComments() throws ApiCallException {

    item = this.discussionController.getStuditModelAcces().getCourseByFagkode("TDT4120");
    discussionController.addCourse(item);

    // Assuming that the discussion is empty,
    // Checks if the first comment is inputed correctly.
    final String comment = "Jeg synes dette er et kjedelig fag";
    discussionController.getInputField().setText(comment);
    clickOn("#information_btn");
    Assertions.assertThat((discussionController.getForumList().getItems().get(0).getKommentar())
        .equals("Jeg synes dette er et kjedelig fag"));

    // Checks if the second comment is inputed correctly.
    final String comment2 = "Uenig, det er et kult fag";
    discussionController.getInputField().setText(comment2);
    clickOn("#information_btn");
    Assertions.assertThat(
        (discussionController.getForumList().getItems().get(1).getKommentar()).equals("Uenig, det er et kult fag"));

    // Returns false if the order of the comments is wrong
    assertFalse((discussionController.getForumList().getItems().get(1).getKommentar())
        .equals("Jeg synes dette er et kjedelig fag"));

    // Checks if the user is the same as the author of the recent comments
    assertTrue((discussionController.getForumList().getItems().get(0).getBrukernavn())
        .equals(discussionController.getCurrentUser().getUsername()));
    assertTrue((discussionController.getForumList().getItems().get(1).getBrukernavn())
        .equals(discussionController.getCurrentUser().getUsername()));


    //Click on upVote
    ListView<Comment> listView = lookup("#forumList").query();
    Node node = lookup("#forumList").query();
    clickOn(node.lookup(".upvoteButton"));

    //Check if upvotes is one for first comment after upvote
    Comment com1 = listView.getItems().get(0);
    assertEquals(1, com1.getUpvotes());

    //Click on upvote again, but it shouldn`t make any difference
    clickOn(node.lookup(".upvoteButton"));
;

    //Check if upvotes is still one for first comment after upvote
    Comment com2 = listView.getItems().get(0);
    assertEquals(1, com2.getUpvotes());
  
    //Click on downVote to make the count zero
    clickOn(node.lookup(".downvoteButton"));
    Comment com3 = listView.getItems().get(0);
    assertEquals(0, com3.getUpvotes());

    //Click on downVote again to make negative one
    clickOn(node.lookup(".downvoteButton"));
    Comment com4 = listView.getItems().get(0);
    assertEquals(-1, com4.getUpvotes());
  }


}