package studit.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import studit.ui.remote.RemoteStuditModelAccess;

public class LoginController {

  @FXML
  PasswordField passwordField;
  @FXML
  TextField usernameField;
  @FXML
  Button login_btn;
  @FXML
  VBox vBox;
  @FXML
  Text registerUser;
  @FXML
  Text forgotPassword;
  @FXML
  BorderPane rootPane;
  @FXML
  Text loginInfoText;

  private RemoteStuditModelAccess remote;

  /**
   * The constrcutur sets up a remote, to call methods from RemoteStuditModelAccess
   * Frontend needs this class to connect to API/backend.
   */
  public LoginController() {
    this.remote = new RemoteStuditModelAccess();
  }

  /**
   * Method to register new user. Loads new window: "Newuser.fxml"
   * 
   */
  public void registerUser() {
    try {
      BorderPane pane = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
      Stage stage = new Stage();
      Scene scene = new Scene(pane);
      stage.setScene(scene);
      stage.setTitle("New User");
      stage.show();
    } catch (Exception e) {
      System.out.println("Coud not open NewUser.fxml");
    }
  }

  /*
   * Checks if email is registered, sends password to user if it is
   */
  public void forgotPassword() {
    // TODO: This.
  }

  /**
   * Checks if login credentials are correct, logs in if it is. Else produces an
   * error message in the application
   */
  public void loginButtonAction() throws Exception {
    String username = usernameField.getText();
    String password = passwordField.getText();
    if (remote.authenticateLogin(username, password)) {
      loginInfoText.setText("");
      BorderPane pane = FXMLLoader.load(getClass().getResource("App.fxml"));
      Scene scene = new Scene(pane);
      //scene.getStylesheets().add(getClass().getResource("listStyles.css").toExternalForm());
      //TODO: trenger vi linjen over?
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("StudIt");
      stage.show();
      Stage stage2 = (Stage) passwordField.getScene().getWindow();
      stage2.hide();
    } else {
      System.out.print("Failure, " + username + ", " + password + " ");
      loginInfoText.setText("Feil brukernavn eller passord");
    }
  }

}
