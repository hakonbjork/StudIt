package studit.ui;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.users.User;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.RemoteStuditModelAccess;

public class CommentListCell extends BorderPane {

  private static RemoteStuditModelAccess remote = new RemoteStuditModelAccess();
  
  User currentUser;
  CourseItem courseItem;
  private Comment comment;
  private Header header = new Header();
  private Body body = new Body();
  private VBox contentWrapper = new VBox(header, body);

  /**
   * Instantiates a new comment cell.
   *
   * @param comment the comment for this particular cell.
   */
  public CommentListCell(Comment comment, User currentUser, CourseItem courseItem) {
    this.comment = comment;
    this.courseItem = courseItem;
    this.currentUser = currentUser;

    try {
      initialize();
    } catch (Exception e) {
      e.printStackTrace();
    }
    setCenter(contentWrapper);
    setMargin(contentWrapper, new Insets(0, 0, 0, 15));
  }

  /**
   * Initialize.
   */
  private void initialize() throws ApiCallException {
    // this.getStyleClass().add("commentListCell");
    header.setTitle(this.comment.getBrukernavn());
    header.setDate(this.comment.getDato());
    body.setComment(this.comment.getKommentar());
    body.setVotes(String.valueOf(comment.getUpvotes()));


    //Listener for upVote button
    body.upvoteButton.setOnAction((event) -> {

      List<String> upVoters = this.comment.getUpvoters();

      if (upVoters.contains(this.currentUser.getUsername())) {
        // Visuell feedback kunne vært implementert
      } else {

        try {

          //Oppdater db etter upvote er klikket.
          CommentListCell.remote.upvoteCommentByID(this.courseItem.getFagkode(), this.currentUser.getUsername(),
              this.comment.getUniqueID());

          int id = this.comment.getUniqueID();

          //Oppdater kommentaren etter bruker har klikket på upVote
          this.comment = CommentListCell.remote.getCommentById(this.courseItem.getFagkode(), id);

          updateView();

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    });

    //Listener for downVote button
    body.downvoteButton.setOnAction((event) -> {

      List<String> downVoters = this.comment.getDownvoters();

      if (downVoters.contains(this.currentUser.getUsername())) {
        //Her kunne vi gitt en visuell feedback
      } else {

        try {

          //Oppdater db etter downvote er klikket.
          CommentListCell.remote.downvoteCommentByID(this.courseItem.getFagkode(), this.currentUser.getUsername(),
              this.comment.getUniqueID());

          int id = this.comment.getUniqueID();

          //Oppdater kommentaren etter bruker har klikket på downVote
          this.comment = CommentListCell.remote.getCommentById(this.courseItem.getFagkode(), id);

          updateView();

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    });

  }

  /**
   * Updates the commentListCellView.
   */
  public void updateView() {
    header.setTitle(this.comment.getBrukernavn());
    header.setDate(this.comment.getDato());
    body.setComment(this.comment.getKommentar());
    body.setVotes(String.valueOf(this.comment.getUpvotes()));

  }

  /**
   * The Class Header.
   */
  static class Header extends AnchorPane {

    private Text title = new Text();
    private Text date = new Text();
    private VBox badge;

    /**
     * Instantiates a new header.
     */
    public Header() {
      setPadding(new Insets(10, 0, 5, 0));

      setLeftAnchor(title, 0.0);
      setBottomAnchor(title, 0.0);
      
      badge = new VBox(date);
      setRightAnchor(badge, 0.0);
      setBottomAnchor(badge, 5.0);
     
      getChildren().addAll(title, badge);
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    private void setTitle(String title) {
      this.title.setText(title);
    }

    /**
     * Sets the date.
     *
     * @param date date
     */
    private void setDate(String date) {
      this.date.setText(date);
    }

  }

  /**
   * The Class Body.
   */
  static class Body extends VBox {

    private Text comment = new Text();
    private Text votes = new Text();
    private Button upvoteButton = new Button("Stem opp");
    private Button downvoteButton = new Button("Stem ned");
   

    /**
     * Instantiates a new body.
     */
    public Body() {
      upvoteButton.getStyleClass().add("upvoteButton");

      downvoteButton.getStyleClass().add("downvoteButton");

      VBox.setVgrow(this, Priority.ALWAYS);
      getChildren().addAll(comment, votes, upvoteButton, downvoteButton);

    }

    /**
     * Sets the comment.
     *
     * @param comment the new description
     */
    private void setComment(String comment) {
      this.comment.setText(comment);
    }

    /**
     * Sets upvotes.
     *
     * @param amount amount of upvotes
     */
    private void setVotes(String votes) {
      this.votes.setText("Stemmer: " + votes);
    }

    public Button getUpvoteButton() {
      return this.upvoteButton;
    }

    public Button getDownvoteButton() {
      return this.downvoteButton;
    }

  }

  // The following methods are used for testing.
  public Body getBody() {
    return this.body;
  }


 
  /**
    * Sets the remote to use for the test.
    *
    * @param remote the remote to set
    */
  public static void setRemote(RemoteStuditModelAccess remote) {
    CommentListCell.remote = remote;
  }

}
