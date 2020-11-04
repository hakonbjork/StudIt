package studit.restapi;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import studit.core.StuditModel;
import studit.core.mainpage.CourseList;
import studit.core.users.Users;

@Path(StuditService.STUDIT_SERVICE_PATH)
public class StuditService {

  public static final String STUDIT_SERVICE_PATH = "studit";

  @Inject
  private StuditModel studitModel;

  /**
   * API base endpoint.
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
   * studit/courses request.
   */
  @Path("/courses")
  public CourseListResource getCourseList() {
    CourseList courseList = getStuditModel().getCourseList();
    return new CourseListResource(courseList);
  }

  /**
   * Returns a new CourseList resource for further processing of the
   * studit/courses request.
   */
  @Path("/users")
  public UsersResource getUsers() {
    Users users = getStuditModel().getUsers();
    return new UsersResource(users);
  }

  @GET
  @Path("/ping")
  public Response ping() {
    return Response.ok().build();
  }

}