package studit.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;

public class CourseListResource {

  private final CourseList courseList;
  private static final Logger LOG = LoggerFactory.getLogger(CourseListResource.class);

  /**
   * Initalizes this CourseListResource with the main CourseList.
   * 
   * @param courseList CourseList object obtained from StuditModel
   */
  public CourseListResource(CourseList courseList) {
    this.courseList = courseList;
  }

  private void checkCourseList() {
    if (this.courseList == null) {
      throw new IllegalArgumentException("No CourseList found");
    }
  }

  /**
   * Gets the active CourseList.
   * 
   * @return the active CourseList
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public CourseList getCourseList() {
    checkCourseList();
    LOG.debug("returning CourseList");
    return this.courseList;
  }

  /**
   * Gets a course by given fagkode.
   * 
   * @param fagkode fagkode of the requested subject.
   * @return response object with CourseItem if fagkode is valid, otherwise 404
   *         not found.
   */
  @GET
  @Path("/{fagkode}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCourseItem(@PathParam("fagkode") String fagkode) {
    CourseItem item = this.courseList.getCourseByFagkode(fagkode);
    if (item == null) {
      LOG.debug("Could not find CourseItem with fagkode '" + fagkode + "'. Returning null'");
      return Response.status(Status.NOT_FOUND).entity("Courseitem with fagkode '" + fagkode + "' does not exist.")
          .build();
    }
    LOG.debug("getCourseItem({})", fagkode);
    return Response.ok(item, MediaType.APPLICATION_JSON).build();
  }

  /**
   * Handles discussion request, and pass it over to the DiscussionResource class
   * 
   * @param fagkode fagkode of the requested discussion
   * @return DiscussionResource if fagkode exists, otherwise null;
   */
  @Path("/{fagkode}/discussion")
  public DiscussionResource getDiscussion(@PathParam("fagkode") String fagkode) {
    CourseItem item = this.courseList.getCourseByFagkode(fagkode);
    if (item == null) {
      LOG.debug("Could not find CourseItem with fagkode '" + fagkode + "'. Returning null'");
      return null;
    }
    LOG.debug("getCourseItem({})", fagkode);
    return new DiscussionResource(item.getDiskusjon());
  }
}