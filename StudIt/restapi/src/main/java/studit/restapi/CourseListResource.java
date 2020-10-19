package studit.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;

public class CourseListResource {

  private final CourseList courseList;
  private static final Logger LOG = LoggerFactory.getLogger(CourseListResource.class);

  /**
   * Initalizes this CourseListResource with the main CourseList
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
   * Gets the active CourseList
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

  @GET
  @Path("/{fagkode}")
  @Produces(MediaType.APPLICATION_JSON)
  public CourseItem getCourseItem(@PathParam("fagkode") String fagkode) {
    CourseItem item = this.courseList.getCourseByFagkode(fagkode);
    if (item == null) {
      LOG.debug("Could not find CourseItem with fagkode '" + fagkode + "'. Returning null'");
      return null;
    }
    LOG.debug("getCourseItem({})", fagkode);
    return item;
  }

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