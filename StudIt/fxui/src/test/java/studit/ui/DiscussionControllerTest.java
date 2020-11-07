package studit.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import javafx.scene.Node;
import java.util.concurrent.TimeoutException;
import org.assertj.core.api.Assertions;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.util.WaitForAsyncUtils;
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

  private DirectStuditModelAccess directStuditModelAccess = new DirectStuditModelAccess();

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader discussionLoader = new FXMLLoader(getClass().getResource("Discussion.fxml"));
    final Parent root = discussionLoader.load();
    this.discussionController = discussionLoader.getController();

    item = this.directStuditModelAccess.getCourseByFagkode("TDT4120");
    discussionController.addCourse(item);
    discussionController.setStuditModelAccess(directStuditModelAccess);

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
  public void testComments() {

    // Assuming that the discussion is empty,
    // Checks if the first comment is inputed correctly.
    String comment = "Jeg synes dette er et kjedelig fag";
    discussionController.getInputField().setText(comment);
    clickOn("#information_btn");
    Assertions.assertThat((discussionController.getForumList().getItems().get(0).getKommentar())
        .equals("Jeg synes dette er et kjedelig fag"));

    // Checks if the second comment is inputed correctly.
    String comment2 = "Uenig, det er et kult fag";
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

  }

  @Test
  public void testUpVoteDuplicate() {

    //Click on upVote
    clickOn(findCommentListCellNode(cell -> true, ".button", 0));

    //Click on upVote again, but nothing will happen because this user has already upvoted
    clickOn(findCommentListCellNode(cell -> true, ".button", 0));

    try {
      Comment comment;
      comment = directStuditModelAccess.getCourseByFagkode("TDT4120").getDiskusjon().getComments().get(0);
      assertEquals(1, comment.getUpvotes());
    
    } catch (ApiCallException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testSelectedListCell() {

    //selects the first item in the list view
    clickOn(findCommentListCellNode(cell -> true, ".button", 0));  
    
    //checks if the same index in the  view is selected
    checkSelectedCommentInt(0);

    //selects the second item in the list view
    clickOn(findCommentListCellNode(cell -> true, ".button", 1));  
    
    //checks if the same index in the  view is selected
    checkSelectedCommentInt(1);

  }


  @Test 
  public void testUpVoteAndThenDownVote(){

  }



  // utility methods
  private Node findNode(Predicate<Node> nodeTest, int num) {
    int count = 0;
    for (Node node : lookup(nodeTest).queryAll()) {
      if (nodeTest.test(node) && count++ == num) {
        return node;
      }
    }
    return null;
  }

  private Node waitForNode(Predicate<Node> nodeTest, int num) {
    WaitForAsyncUtils.waitForFxEvents();
    Node[] nodes = new Node[1];
    try {
      WaitForAsyncUtils.waitFor(2000, TimeUnit.MILLISECONDS,
          () -> {
            while (true) {
              if ((nodes[0] = findNode(nodeTest, num)) != null) {
                return true;
              }
              Thread.sleep(100);
            }
          }
      );
    } catch (TimeoutException e) {
      fail("No appropriate node available");
    }
    return nodes[0];
  }

  //private CommentListCell findCommentListCell(Predicate<CommentListCell> test, int num) {
  //  return (CommentListCell) waitForNode(node -> node instanceof CommentListCell && test.test((CommentListCell) node), num);
  //}

  private Node findCommentListCellNode(Predicate<CommentListCell> test, String selector, int num) {
    Node listCell = waitForNode(node -> node instanceof CommentListCell && (selector == null || node.lookup(selector) != null) && test.test((CommentListCell) node), num);
    return listCell.lookup(selector);
  }

  private void checkSelectedCommentInt (int index) {
    final ListView<Comment> commentListView = lookup("#forumList").query();
    assertEquals(index, commentListView.getSelectionModel().getSelectedIndex());
  }


  
}