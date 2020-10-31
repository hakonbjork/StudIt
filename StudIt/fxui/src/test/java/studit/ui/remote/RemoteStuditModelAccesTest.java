package studit.ui.remote;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.restserver.DefaultGenerator;
import studit.restserver.StuditConfig;
import studit.restserver.StuditModuleObjectMapperProvider;
import studit.ui.TestServer;

public class RemoteStuditModelAccesTest extends TestServer {

  /**
   * To prevent multiple server restarts, we only use the @Test annotation when a
   * strict server restart is required (every time a new Test instance is created
   * the server is restarted)
   */

  private RemoteStuditModelAccess remoteModel = new RemoteStuditModelAccess(true);

  /**
   * The bulk of the functionallity will be tested here (prevents unnessecary server restartss)
   * @throws Exception ignore.
   */
  @Test
  public void tests() throws Exception {

  }

  @Test
  public void testPing() throws Exception {
    assertTrue(remoteModel.ping());
    remoteModel.setTestEndpointPath("http://localhost:9998/studet");
    assertFalse(remoteModel.ping());
    super.tearDown();
    assertFalse(remoteModel.ping());
  }
}