package studit.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.users.User;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.RemoteStuditModelAccess;

public class DiscussionController implements Initializable {

  private static Boolean testingMode = false;

  private RemoteStuditModelAccess remoteStuditModelAccess = new RemoteStuditModelAccess();

  // The currentUser is set via courseController
  private User currentUser = new User("Ida Idasen", "IdaErBest", "IdaElskerHunder@flyskflysk.com",
      "0f0b30a66731e73240b9e331116b57de84f715ab2aea0389bb68129fcf099da3", 1);

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
  private Button mainpage_btn;

  @FXML
  private Button chatbot_btn;

  @FXML
  private Button logoutAction;

  @FXML
  private TextField newPostInputField;

  @FXML
  private Button newPostButton;

  @FXML
  private ListView<Comment> forumList;

  private ObservableList<Comment> listView = FXCollections.observableArrayList();

  private CourseItem courseItem;

  public void addCourse(CourseItem name) {
    this.courseItem = name;
  }

  /**
   * Method which handles when user adds a a new post.
   */
  @FXML
  void addNewPost(ActionEvent event) {

    String input = newPostInputField.getText();
    try {
      remoteStuditModelAccess.addCommentToDiscussion(this.courseItem.getFagkode(), this.currentUser.getName(),
          input);
      updateView();
      newPostInputField.clear();
    } catch (ApiCallException e) {
      newPostInputField.clear();
      // Her kunne vi gitt brukeren en visuell feedback.
      e.printStackTrace();
    }
  }

  /**
   * Method to update the view after actions that changes attributes in the core
   * are triggered.
   */
  public void updateView() {

    try {

      this.courseItem = remoteStuditModelAccess.getCourseByFagkode(this.courseItem.getFagkode());
      setFagkode();
      setFagnavn();
      loadView();

    } catch (ApiCallException e) {
      e.printStackTrace();
    }

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

  /**
   * Opens main-page scene and closes the current scene.
   */
  @FXML
  void handleMainPageAction(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
      Parent root = loader.load();
      AppController appController = (AppController) loader.getController();
      appController.setCurrentUser(this.currentUser);

      Stage stage2 = new Stage();
      stage2.setScene(new Scene(root));
      stage2.setTitle("App");
      stage2.show();

      Stage stage = (Stage) rootPane.getScene().getWindow();
      stage.hide();

    } catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
   * Opens Chatbot.
   */
  @FXML
  void openChatBot(ActionEvent event) {
    if (AppController.getChatbot() == null) {
      AppController.newChatbot();
    } else {
      AppController.getChatbot().show();
    }
  }

  public void setFagkode() {
    String label = this.courseItem.getFagkode();
    this.fagkode.setText(label);
  }

  public void setFagnavn() {
    String label = this.courseItem.getFagnavn();
    this.fagnavn.setText(label);
  }

  /**
   * Opens the information page about the course.
   */
  @FXML
  void openInformationTab(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Course.fxml"));
      Parent root = loader.load();
      CourseController courseController = (CourseController) loader.getController();
      courseController.setCourseItem(this.courseItem);
      courseController.updateView();

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  /**
   * Loads the list View and sets the text for the labels in the page.
   */
  public void loadView() {

    // This method clears the list each time the method is triggered in order to
    // keep duplicates away.
    listView.clear();

    if (this.courseItem.getDiskusjon() != null) {

      this.fagnavn.setText(this.courseItem.getFagnavn());

      this.fagkode.setText(this.courseItem.getFagkode());

      for (Comment comment : this.courseItem.getDiskusjon().getComments().values()) {

        listView.add(comment);

      }

      forumList.setItems(listView);

      // for each Comment in the ListView, a custom ListCell is instantiated
      forumList.setCellFactory(param -> new ListCell<Comment>() {

        private CommentListCell commentListCell;

        @Override
        public void updateItem(Comment comment, boolean empty) {
          super.updateItem(comment, empty);
          if (empty) {
            setText(null);
            setGraphic(null);
            return;
          }

          int id = comment.getUniqueID();

          try {
            Comment com = remoteStuditModelAccess.getCommentById(courseItem.getFagkode(), id);
            commentListCell = new CommentListCell(com, currentUser, courseItem);
            setGraphic(commentListCell);
          } catch (ApiCallException e) {
            e.printStackTrace();
            System.out.println(
                "Det utløses en ApiCallException når custom listCellene settes. Les feilmeldingen som følger for å få mer info");
            System.out.println(e.getMessage());
            forumList.setItems(listView);
          }

        }
      });

    } else {
      System.out.println("Dette fagets diskusjon har enda ingen kommentarer");
      forumList.setItems(listView);
    }
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  // The following methods are used for test purposes.
  public TextField getInputField() {
    return this.newPostInputField;
  }

  public ListView<Comment> getForumList() {
    return this.forumList;
  }

  public void setStuditModelAccess(RemoteStuditModelAccess r) {
    this.remoteStuditModelAccess = r;
    CommentListCell.setRemote(r);
  }

  public RemoteStuditModelAccess getStuditModelAcces() {
    return this.remoteStuditModelAccess;
  }

  public User getCurrentUser() {
    return this.currentUser;
  }

   public static void setTestingMode(Boolean bol) {
    if (bol) {
      testingMode = true;
    } else {
      testingMode = false;
    }
  }

  public static Boolean getTestingMode() {
    return testingMode;
  }

}