package studit.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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

  private String courseName;

  public void addCourse(String name){
    this.courseName = name;
  }

  public void initialize(URL location, ResourceBundle resources) throws ApiCallException {

    this.fagnavn.setText(this.courseName);

    //TODO fikse riktig course, så må få info fra courseController elns
    Discussion discussion = remoteStuditModelAccess.getDiscussion(this.courseName);
    
    for (Comment comment : discussion.getComments().values()) {

      listView.add(comment);
    
    }

    ////**FO-data */
    Comment comment1 = new Comment();
    comment1.setBrukernavn("Mithu");
    comment1.setKommentar("kommentar");
    listView.add(comment1);
    /////////////////////////////////

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

 /**
   * logs user out, and goes to login scene closes the current window.
   */
  @FXML
  void handleLogoutAction(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
      Parent root = loader.load();

      Stage stage2 = new Stage();
      stage2.setScene(new Scene(root));
      stage2.setTitle("Login");
      stage2.show();

      Stage stage = (Stage) rootPane.getScene().getWindow();
      stage.hide();

    } catch (IOException e) {
      System.out.println(e);
    }
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
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Course.fxml"));
      Parent root = loader.load();

      Stage stage2 = new Stage();
      stage2.setScene(new Scene(root));
      stage2.setTitle("Course");
      stage2.show();

      Stage stage = (Stage) rootPane.getScene().getWindow();
      stage.hide();

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @FXML
  void addNewPost(ActionEvent event) {

}


}


