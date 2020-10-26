package studit.restserver;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import studit.core.StuditModel;
import studit.json.StuditPersistence;
import studit.restapi.StuditService;

public class StuditConfig extends ResourceConfig {

  private StuditModel studitModel;
  private static final String DBPATH = "res/db/studitModel.json";

  /**
   * Initialize StuditConfig and register appropriate classes.
   * 
   * @param studitModel conists of our database object with functionallity
   */
  public StuditConfig(StuditModel studitModel) {
    setStuditModel(studitModel);
    register(StuditService.class);
    register(StuditModuleObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(StuditConfig.this.studitModel);
      }
    });
    //DefaultGenerator.writeDefaultDataToDb();
  }

  public StuditConfig() {
    this(loadModel(DBPATH));
  }

  /**
   * This is used exclusively for testing and initializing a default model
   * @param path test path
   */
  public StuditConfig(String path) {
    this(DefaultGenerator.writeDefaultDataToDb(path));
  }

  public StuditModel getStuditModel() {
    return studitModel;
  }

  public void setStuditModel(StuditModel studitModel) {
    this.studitModel = studitModel;
  }

  public static StuditModel loadModel(String path) {

    StuditPersistence studitPersistence = new StuditPersistence();
    StuditModel model = null;

    try (Reader reader = new FileReader(path, StandardCharsets.UTF_8)) {
      model = studitPersistence.readStuditModel(reader);
    } catch (IOException e) {
      System.out.println("Couldn't read studitModel.json --> Creating empty object(" + e + ")");
    }
    return model == null ? DefaultGenerator.writeDefaultDataToDb(path) : model;
  }

}