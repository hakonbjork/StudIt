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

  private CourseItem courseItem;

  public void addCourse(CourseItem name) {
    this.courseItem = name;
  }

  //TODO Vi må fikse at brukeren er en variabel som er tilgjengelig i alle controllers slik at kind of er en global variabel.
  //Pluss se på API i forhold til add comment.

  //@FXML
  //void addNewPost(ActionEvent event) {
  //  User user = new User();
  //  String input = newPostInputField.getText();
  //  try {
  //    remoteStuditModelAccess.addCommentToDiscussion(this.courseItem.getFagkode(), user.getName(), input);
  //    updateView();
  //    newPostInputField.clear();
  //  } catch (ApiCallException e) {
  //    e.printStackTrace();
  //  }
  //
  //
  //}

  public void updateView(){
    
    try {
      courseItem = remoteStuditModelAccess.getCourseByFagkode(this.courseItem.getFagkode());

      loadView();

    } catch (ApiCallException e) {
      // TODO Auto-generated catch block
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
  void handleMainPageAction(ActionEvent actionEvent) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
      Parent root = loader.load();

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

  //TODO skal denne være her?
  @FXML
  void handleNtnuAction(ActionEvent event) {

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

  @FXML
  void openInformationTab(ActionEvent event){
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


  //TODO funker ikke :()
  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void loadView(){

    if(this.courseItem.getDiskusjon() != null){

      this.fagnavn.setText(this.courseItem.getFagnavn());

      this.fagkode.setText(this.courseItem.getFagkode());

      for (Comment comment : this.courseItem.getDiskusjon().getComments().values()) {

      listView.add(comment);
    
      }

      if (!listView.isEmpty()) {

          forumList.setItems(listView);

          forumList.setCellFactory(lv -> new ListCell<Comment>() {
            //brukernavn

            //dato

            //kommentar
            private final Label label = new Label();

            //upVotes

            //downVotes
            
            @Override
            protected void updateItem(Comment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    label.setText(item.getKommentar());
                    setGraphic(label);
                }
            }
        });

      } else {

        System.out.println("faile med listcell");

      }
  
    } else{

      System.out.println("Kunne ikke loade discussion.fxml med informasjon");

      System.out.println(this.courseItem + " hvis det printes null til venstre er det noe feil med passing av infomrasjon mellom scener");

      Comment comment = new Comment();

      listView.add(comment);
      forumList.setItems(listView);
    }
  

  }

}


