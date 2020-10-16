package studit.restserver;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import studit.core.StuditModel;
import studit.restapi.StuditService;

public class StuditConfig extends ResourceConfig {

  private StuditModel studitModel;

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
  }

  public StuditConfig() {
    this(createDefaultStuditModel());
  }

  public StuditModel getTodoModel() {
    return studitModel;
  }

  public void setTodoModel(StuditModel studitModel) {
    this.studitModel = studitModel;
  }

  public static StuditModel createDefaultStuditModel() {
    return new StuditModel();
  }


}