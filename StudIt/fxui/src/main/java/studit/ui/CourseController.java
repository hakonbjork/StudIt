package studit.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class CourseController implements Initializable {

  @FXML
  private BorderPane rootPane;

  @FXML
  private Button information_btn;

  @FXML
  private Button discussion_btn;

  @FXML
  private Label fagkode;

  @FXML
  private Label fagnavn;

  @FXML
  private Label rating;

  @FXML
  private TextArea courseInformation;

  @FXML
  private TextArea litterature;

  @FXML
  private TextArea tips_tricks;

  @FXML
  private Button mainPageAction;

  @FXML
  private Button chatbot_btn;

  @FXML
  private Button logoutAction;

  @FXML
  private TextField eksamensdato;

  @FXML
  private TextField vurderingsform;

  @FXML
  private TextField hjelpemidler;

  @FXML
  private TextArea commentField;

  @FXML
  private Button addCommentAction;

  private Scene mainScene;

  public void setMainScene(Scene scene) {
    this.mainScene = scene;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  /**
   * Function to set the label - the name of the subject on the top of the page.
   * 
   * @param label the label to set
   */

  // @FXML
  // public void setLabel(String label) {
  // this.label.setText(label);
  // }

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
      Stage primaryStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
      primaryStage.setScene(mainScene);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Gives the user a choice to open NTNU homepage in web-browser.
   */
  @FXML
  void handleNtnuAction(ActionEvent event) {
    System.out.println("NTNU");
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

  /**
   * Sets the rating of the subject.
   * 
   * @param rating represents the value of the rating to be set
   */
  @FXML
  public void setRating(double rating) {
    String r = String.valueOf(rating);
    this.rating.setText(r);
  }

  /**
   * returns the rating of the subject.
   */
  @FXML
  public double getRating() {
    return Double.valueOf(this.rating.getText());
  }

  // @FXML
  // public void setCourseText(String text) {
  // this.courseText.setText(text.toString());
  // }

  @FXML
  void openDiscussion(ActionEvent event) {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Discussion.fxml"));
      Parent root = loader.load();

      Stage stage2 = new Stage();
      stage2.setScene(new Scene(root));
      stage2.setTitle("Discussion");
      stage2.show();

      Stage stage = (Stage) rootPane.getScene().getWindow();
      stage.hide();

    } catch (IOException e) {
      System.out.println(e);
    }

  }

  @FXML
  void handleAddCommentAction(ActionEvent event) {
    // TODO fikse metoden her.
    System.out.println("Adda en tilbakemelding");
  }

}
