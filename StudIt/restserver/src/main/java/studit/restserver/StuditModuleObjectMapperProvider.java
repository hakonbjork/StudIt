package studit.restserver;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;
import studit.json.StuditModule;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StuditModuleObjectMapperProvider implements ContextResolver<ObjectMapper> {
  private final ObjectMapper objectMapper;

  public StuditModuleObjectMapperProvider() {
    objectMapper = new ObjectMapper().registerModule(new StuditModule());
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return objectMapper;
  }

}