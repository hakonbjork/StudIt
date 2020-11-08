package studit.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import studit.core.users.User;
import studit.ui.remote.DirectStuditModelAccess;

public class LoginControllerTest extends ApplicationTest {

  @Mock
  private LoginController loginController;
  private DirectStuditModelAccess remote;

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
    assertTrue(loginController.usernameField.getText().equals("user"));
  }

  // @Test
  // public void testLoginButtonAction() {
  //   writeUserPassword();
  //   clickOn("#login_btn");
  //   FxAssert.verifyThat(window("StudIt"), WindowMatchers.isShowing());
  // }

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

  // private void changeControllerMethod() {
  //   doAnswer(new answer(InvocationOnMock invocation){
  //     public Void answer(InvocationOnMock invocation) {
  //     Object[] args = invocation.getArguments();
  //     System.out.println("called with arguments: " + Arrays.toString(args));
  //     return null;
  //   }
  //   }).when(loginController.loginButtonAction());



}
