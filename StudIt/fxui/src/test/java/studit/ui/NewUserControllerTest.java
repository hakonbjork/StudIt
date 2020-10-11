package studit.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import studit.core.User;
import studit.core.UserManager;

public class NewUserControllerTest extends ApplicationTest {

  private NewUserController controller;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("NewUser.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testSaveNewUserAction() {
    User user = new User("Ola Halvorsen", "olahalla", "ola.halvorsen@gmail.com", "olala");
    UserManager.addUser(user);
    writeNewUserFields();
    clickOn("#saveNewUserButton");
    assertTrue(controller.infoTextField.getText().equals("Error: This username is already taken"));
  }

  public void writeNewUserFields() {
    clickOn("#nameField").write("Ola Halvorsen");
    clickOn("#usernameField").write("olahalla");
    clickOn("#mailField").write("ola.halvorsen@gmail.com");
    clickOn("#userPasswordField").write("olala");
  }

}