package studit.ui.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.ws.rs.core.Response;

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

public class RemoteStuditModelAccesTest extends JerseyTest {

  RemoteStuditModelAccess remoteModel = new RemoteStuditModelAccess();
  protected final boolean DEBUG = true;
  protected ObjectMapper mapper;
  protected StuditModel defaultModel = DefaultGenerator
      .writeDefaultDataToDb("studit/fxui/defaultdb.json");

  @Override
  protected StuditConfig configure() {
    final StuditConfig config = new StuditConfig("studit/fxui/defaultdb.json");
    if (DEBUG) {
      enable(TestProperties.LOG_TRAFFIC);
      enable(TestProperties.DUMP_ENTITY);
      config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
    }
    return config;
  }

  @Override
  protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
    return new GrizzlyTestContainerFactory();
  }

  @Override
  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    mapper = new StuditModuleObjectMapperProvider().getContext(getClass());
  }

  @AfterEach
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testPing() {
    assertTrue(remoteModel.ping());
  }
}