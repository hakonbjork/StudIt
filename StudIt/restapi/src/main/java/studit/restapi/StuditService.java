package studit.restapi;

import studit.core.StuditModel;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Path(StuditService.STUDIT_SERVICE_PATH)
public class StuditService {

  public static final String STUDIT_SERVICE_PATH = "studit";
  private static final Logger LOG = LoggerFactory.getLogger(StuditService.class);

  @Inject
  private StuditModel studitModel;

  /**
   * API base endpoint
   * @return active StuditModel object
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public StuditModel getStuditModel() {
    return studitModel;
  }

}