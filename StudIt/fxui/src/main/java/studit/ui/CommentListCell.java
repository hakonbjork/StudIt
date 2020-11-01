package studit.ui;

import java.io.IOException;
import java.lang.System.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import studit.core.mainpage.Comment;
import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import studit.core.mainpage.Comment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import studit.core.users.User;
import studit.ui.remote.RemoteStuditModelAccess;

import java.util.List;

 public class CommentListCell extends BorderPane {

      //RemoteStuditModelAccess remote = new RemoteStuditModelAccess();

      //TODO fikse en måte å hente user
      User fakeUser = new User();
      private Comment comment;
      private Header header = new Header();
      private Body body = new Body();
      private VBox contentWrapper = new VBox(header, body);
      

      /**
	     * Instantiates a new comment cell.
	    *
	    * @param comment the comment for this particular cell.
	    */
	    public CommentListCell(Comment comment) {
		    this.comment = comment;
		    initialize();
	    	setCenter(contentWrapper);
        setMargin(contentWrapper, new Insets(0, 0, 0, 15));
      }

      
      /**
	    * Initialize.
	    */
	    private void initialize() {
		  //this.getStyleClass().add("commentListCell");
        header.setTitle(this.comment.getBrukernavn());
		    header.setDate(this.comment.getDato());
        body.setComment(this.comment.getKommentar());
        body.setUpvotes(String.valueOf(comment.getUpvotes()));

        //TOOD Foo-method, må endre
        this.fakeUser.setUsername("Mithu");
        body.button.setOnAction((event) -> {
        
        List<String> upVoters = this.comment.getUpvoters();

        if(upVoters.contains(this.fakeUser.getUsername())){

          //remote send slett denne brukeren fra listen
          //hent kommentar tilbake og loadView() på nytt for denne cellen.

        } else {

          //remote send legg til brukeren til listen
          //hent kommentar tilbake og loadView på nytt for denne cellen.

        }

        });

        //updateView();
      
      }
      

      //**********************TODO - Når API er fikset kan denne unkommenteres *****************************'/
      /**
       * Updates the commentListCellView
       */
      //public void updateView(){
      //  Comment comment1 = remote.getCommentByUniqueId(this.comment.getUniqueID());
      //  this.comment = comment1;
      //  header.setTitle(this.comment.getBrukernavn());
		  //  header.setDate(this.comment.getDato());
      //  body.setComment(this.comment.getKommentar());
      //  body.setUpvotes(String.valueOf(comment.getUpvotes()));
      //}



      /**
      * The Class Header.
      */
      static private class Header extends AnchorPane {
      
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
        //****************TODO***************************************************************************/
        //title.getStyleClass().add("title");
        
        badge = new VBox(date);
        setRightAnchor(badge, 0.0);
        setBottomAnchor(badge, 5.0);
        //****************TODO***************************************************************************/
        //badge.getStyleClass().add("state");
        
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
		  * Sets the date
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
    static private class Body extends VBox {
      
      private Text comment = new Text();
      private Text upvotes = new Text();
      private Button button = new Button("Upvote");

      /**
       * Instantiates a new body.
       */
      public Body() {
        //****************TODO***************************************************************************/
        //comment.getStyleClass().add("comment");
        
        VBox.setVgrow(this, Priority.ALWAYS);
        getChildren().addAll(comment, upvotes, button);

        
      }
      
      /**
       * Sets the comment
       *
       * @param comment the new description
       */
      private void setComment(String comment) {
        this.comment.setText(comment);
      }

      /**
       * Sets upvotes
       *
       * @param amount amount of upvotes
       */
      private void setUpvotes(String upvotes) {
        this.upvotes.setText(upvotes);
      }


    }
  }