package studit.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginControllerTest extends ApplicationTest {

  private LoginController controller;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testLoginController() {
    assertNotNull(this.controller);
  }

  // @Test
  // public void testLogoutAction() {
  //   clickOn("#registerUser_btn");
  //   ObservableList<Window> windows = Window.getWindows();
  //   Window login = controller.passwordField.getScene().getWindow();
  //   Assertions.assertThat(windows.contains(login));
  //   FxAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
  // }

  @Test
  

}