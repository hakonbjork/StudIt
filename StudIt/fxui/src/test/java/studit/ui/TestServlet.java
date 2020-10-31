package studit.ui;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import studit.core.StuditModel;
import studit.restserver.DefaultGenerator;
import studit.restserver.StuditConfig;
import studit.restserver.StuditModuleObjectMapperProvider;

public class TestServlet {

  private JerseyTest jerseyTest;
  private final boolean DEBUG = false;
  protected StuditModel defaultModel = DefaultGenerator
      .writeDefaultDataToDb("src/test/resources/studit/fxui/defaultdb.json");
  protected ObjectMapper mapper;

  public void setUp() throws Exception {
    jerseyTest.setUp();
    mapper = new StuditModuleObjectMapperProvider().getContext(getClass());
  }

  public TestServlet() {
    jerseyTest = new JerseyTest() {
      @Override
      protected StuditConfig configure() {
        final StuditConfig config = new StuditConfig("src/test/resources/studit/fxui/defaultdb.json");
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

    };

    try {
      setUp();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @return the jerseyTest
   */
  public JerseyTest getJerseyTest() {
    return jerseyTest;
  }

  /**
   * @return the defaultModel
   */
  public StuditModel getDefaultModel() {
    return defaultModel;
  }

  /**
   * @param defaultModel the defaultModel to set
   */
  public void setDefaultModel(StuditModel defaultModel) {
    this.defaultModel = defaultModel;
  }

}