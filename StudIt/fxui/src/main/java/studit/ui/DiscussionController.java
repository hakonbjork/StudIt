package studit.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import studit.core.mainpage.Comment;
import studit.core.mainpage.Discussion;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.RemoteStuditModelAccess;

public class DiscussionController {

  RemoteStuditModelAccess remoteStuditModelAccess = new RemoteStuditModelAccess();

  @FXML
  private BorderPane rootPane;

  @FXML
  private Button information_btn;

  @FXML
  private Button forum_btn;

  @FXML
  private Label fagkode;

  @FXML
  private Label fagnavn;

  @FXML
  private Button mainPageAction;

  @FXML
  private Button chatbot_btn;

  @FXML
  private Button logoutAction;

  @FXML
  private TextField newPostInputField;

  @FXML
  private Button handleAddNewPost;

  @FXML
  private ListView<Comment> forumList;

  private ObservableList<Comment> listView = FXCollections.observableArrayList();

  // add some Students

  public void initialize(URL location, ResourceBundle resources) throws ApiCallException {

    // Henter en liste med ForumPoster fra CourseController som har data fra DB om
    // det spesifike kurset.

    Discussion discussion = remoteStuditModelAccess.getDiscussion("TDT4109");
    
    discussion.getComments();

    for (Comment comment : discussion.getComments().values()) {

      listView.add(comment);
    
    }

    forumList.setItems(listView);

    forumList.setCellFactory(param -> new CommentListCell());
    
  }

  @FXML
  void handleAddNewPost(ActionEvent event) {
    //TODO hent brukernavn fra innlogget bruker
    String username = "user";
    Comment comment = new Comment();
    publishPost(comment);
  }

  public void publishPost(Comment comment){
    
  }

  @FXML
  void handleLogoutAction(ActionEvent event) {

  }

  @FXML
  void handleMainPageAction(ActionEvent event) {

  }

  @FXML
  void handleNtnuAction(ActionEvent event) {

  }

  @FXML
  void openChatBot(ActionEvent event) {

  }

  @FXML
  void openInformationTab(ActionEvent event){
    //TODO
  }

  @FXML
  void addNewPost(ActionEvent event) {

} 
}


