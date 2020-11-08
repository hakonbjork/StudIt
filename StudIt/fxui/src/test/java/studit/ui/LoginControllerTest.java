package studit.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import studit.core.users.User;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;

public class LoginControllerTest extends ApplicationTest {

  private LoginController loginController;
  private final RemoteStuditModelAccess remote = new DirectStuditModelAccess();
  // private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  // private final PrintStream originalOut = System.out;

  // @Before
  // public void setUpStreams() {
  //   System.setOut(new PrintStream(outContent));
  // }

  // @After
  // public void restoreStreams() {
  //   System.setOut(originalOut);
  // }

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
    loginController.setRemote(this.remote);
    try {
      ApplicationTest.launch(LoginApp.class);
    } catch (Exception e) {
      fail();
      e.printStackTrace();
    }
    assertNotNull(this.loginController);
  }

  @Test
  public void testUserPasswordFields() {
    writeUserPassword();
    assertTrue(loginController.usernameField.getText().equals("Berte92"));
  }

  @Test
  public void testLoginButtonAction() {
    loginController.setRemote(this.remote);
    writeUserPassword();
    clickOn("#passwordField").write("fweg");
    clickOn("#login_btn");
    assertEquals("Feil brukernavn eller passord", loginController.loginInfoText.getText());
  }

  @Test
  public void testRegisterUserAction() {
    loginController.setRemote(this.remote);
    clickOn("#registerUser_btn");
    FxAssert.verifyThat(window("New User"), WindowMatchers.isShowing());
  }

  public void writeUserPassword() {
    String u = "Berte92";
    String p = "kusma1992";
    clickOn("#usernameField").write(u);
    clickOn("#passwordField").write(p);
  }

}
