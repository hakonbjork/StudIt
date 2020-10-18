package studit.restserver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
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
   * Initialize StuditConfig and register appropriate classes
   * @param studitModel conists of our database object with functionallity
   */
  public StuditConfig(StuditModel studitModel) {
    setTodoModel(studitModel);
    register(StuditService.class);
    register(StuditModuleObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(StuditConfig.this.studitModel);
      }
    });
    DefaultGenerator.writeDefaultDataToDb();
  }

  public StuditConfig() {
    this(loadModel());
  }

  public StuditModel getTodoModel() {
    return studitModel;
  }

  public void setTodoModel(StuditModel studitModel) {
    this.studitModel = studitModel;
  }

  public static StuditModel loadModel() {

    StuditPersistence studitPersistence = new StuditPersistence();
    StuditModel model = null;

    try (Reader reader = new FileReader(DBPATH, StandardCharsets.UTF_8)) {
      model = studitPersistence.readStuditModel(reader);
    } catch (IOException e) {
      System.out.println("Couldn't read studitModel.json --> Creating empty object(" + e + ")");
    }
    return model == null ? createDefaultStuditModel(studitPersistence) : model;
  }

  public static StuditModel createDefaultStuditModel(StuditPersistence studitPersistence) {
    StuditModel model = new StuditModel();
    try {
      Writer writer = new OutputStreamWriter(new FileOutputStream(DBPATH), StandardCharsets.UTF_8);
      studitPersistence.writeStuditModel(model, writer);
    } catch (FileNotFoundException e) {
      System.out.println("Error -> Packaging structure has changed, please update StuditConfig resource path");
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return model;
  }

}