package studit.restapi;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import studit.core.StuditModel;
import studit.core.mainpage.CourseList;
import studit.core.users.Users;

@Path(StuditService.STUDIT_SERVICE_PATH)
public class StuditService {

  public static final String STUDIT_SERVICE_PATH = "studit";
  private static final Logger LOG = LoggerFactory.getLogger(StuditService.class);

  @Inject
  private StuditModel studitModel;

  /**
   * API base endpoint
   * 
   * @return active StuditModel object
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public StuditModel getStuditModel() {
    return studitModel;
  }

  /**
   * Returns a new CourseList resource for further processing of the
   * studit/courses request
   */
  @Path("/courses")
  public CourseListResource getCourseList() {
    CourseList courseList = getStuditModel().getCourseList();
    LOG.debug("Accessing '/courses' base endpoint...");
    return new CourseListResource(courseList);
  }

   /**
   * Returns a new CourseList resource for further processing of the
   * studit/courses request
   */
  @Path("/users")
  public UsersResource getUsers() {
    Users users = getStuditModel().getUsers();
    LOG.debug("Accessing '/users' base endpoint...");
    return new UsersResource(users);
  }

}