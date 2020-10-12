package studit.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginControllerTest extends ApplicationTest {

  private LoginController loginController;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
    final Parent root = loader.load();
    this.loginController = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testLoginController() {
    assertNotNull(this.loginController);
  }

  @Test
  public void testUserPasswordFields() {
    writeUserPassword();
    assertTrue(loginController.usernameField.getText().equals("user"));
  }

  @Test
  public void testLoginButtonAction() {
    writeUserPassword();
    clickOn("#login_btn");
    FxAssert.verifyThat(window("StudIt"), WindowMatchers.isShowing());
  }

  @Test
  public void testRegisterUserAction() {
    clickOn("#registerUser_btn");
    FxAssert.verifyThat(window("New User"), WindowMatchers.isShowing());
  }

  public void writeUserPassword() {
    String u = "user";
    String p = "password";
    clickOn("#usernameField").write(u);
    clickOn("#passwordField").write(p);
  }



}