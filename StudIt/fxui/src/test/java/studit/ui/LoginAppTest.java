package studit.ui;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;

public class LoginAppTest extends ApplicationTest{

  @Test
  public void testLoginApp() throws Exception {
    ApplicationTest.launch(LoginApp.class);
    FxAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
  }

}