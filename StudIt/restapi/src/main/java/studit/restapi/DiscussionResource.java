package studit.restapi;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studit.core.mainpage.Discussion;

public class DiscussionResource {

  private final Discussion discussion;
  private static final Logger LOG = LoggerFactory.getLogger(CourseListResource.class);

  /**
   * Initalizes this DiscussionResource with the requested discussion
   * 
   * @param dicussion Discussion object obtained from the requested course
   */
  public DiscussionResource(Discussion discussion) {
    this.discussion = discussion;
  }

  private void checkDiscussion() {
    if (this.discussion == null) {
      throw new IllegalArgumentException("No Discussion found");
    }
  }

  /**
   * Gets the active CourseList
   * 
   * @return the active CourseList
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Discussion getCourseList() {
    checkDiscussion();
    LOG.debug("returning Discussion");
    return this.discussion;
  }

  @POST
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addComment(@QueryParam("username") String username, @QueryParam("comment") String comment) {
    if (username == null || comment == null) {
      LOG.debug("Failed to execute /add request on discussion - missing params");
      return Response.serverError().entity("Error -> both username and comment must be passed as params").build();
    }
    LOG.debug("Adding new comment to discussion");
    return Response.ok(discussion.addComment(username, comment), MediaType.APPLICATION_JSON).build();
  }

  /**
   * Deletes a comment from the database
   * @param id unique comment id
   * @return 404 if invalid id, otherwise 204
   */
  @DELETE
  @Path("/remove/{id}")
  public Response removeComment(@PathParam("{id}") int id) {
    boolean removed = discussion.removeComment(id);
    if (!removed) {
      LOG.debug("Failed to remove comment with id '" + id + "'. Comment does not exist");
      return Response.status(Status.NOT_FOUND).entity("comment with id '" + id + "' does not exist").build();
    }
    LOG.debug("Succesfully to removed comment with id '" + id + "'");
    return Response.noContent().build();
  }

  @PUT
  @Path("/updove/{id}")
  public Response upvoteComment() {
    return null;
  }
}