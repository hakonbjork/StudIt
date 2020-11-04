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
import studit.core.mainpage.Comment;
import studit.core.mainpage.Discussion;

public class DiscussionResource {

  private final Discussion discussion;
  private static final Logger LOG = LoggerFactory.getLogger(CourseListResource.class);

  /**
   * Initalizes this DiscussionResource with the requested discussion.
   * 
   * @param discussion Discussion object obtained from the requested course
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
   * Gets the active Discussion.
   * 
   * @return the active Discussion
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Discussion getDiscussion() {
    checkDiscussion();
    LOG.debug("returning Discussion");
    return this.discussion;
  }

  /**
   * Get a comment with unique id.
   * @param id unique comment id
   * @return 200 ok with comment if it exists, otherwise 404 not found.
   */
  @GET
  @Path("/comment/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCourseList(@PathParam("id") int id) {
    Comment comment = discussion.getCommentByID(id);

    if (comment == null) {
      LOG.debug("Requested comment with id'" + id + "' does not exist");
      return Response.status(Status.NOT_FOUND).entity("Comment with id '" + id + "' doesn't exist").build();
    }

    return Response.ok(comment, MediaType.APPLICATION_JSON).build();
  }

  /**
   * Adds a comment to the Discussion.
   * 
   * @param username username of the person who wrote the comment
   * @param comment  comment
   * @return 400 bad request if failed, 200 OK if successful
   */
  @POST
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addComment(@QueryParam("username") String username, @QueryParam("comment") String comment) {
    if (username == null || comment == null) {
      LOG.debug("Failed to execute /add request on discussion - missing params");
      return Response.status(Status.BAD_REQUEST).entity("Error -> both username and comment must be passed as params")
          .build();
    }
    LOG.debug("Adding new comment to discussion");
    return Response.ok(discussion.addComment(username, comment), MediaType.APPLICATION_JSON).build();
  }

  /**
   * Deletes a comment from the database.
   * 
   * @param id unique comment id
   * @return 404 if invalid id, otherwise 204
   */
  @DELETE
  @Path("/remove/{id}")
  public Response removeComment(@PathParam("id") int id) {
    LOG.debug(String.valueOf(id));
    boolean removed = discussion.removeComment(id);
    if (!removed) {
      LOG.debug("Failed to remove comment with id '" + id + "'. Comment does not exist");
      return Response.status(Status.NOT_FOUND).entity("comment with id '" + id + "' does not exist").build();
    }
    LOG.debug("Succesfully removed comment with id '" + id + "'");
    return Response.noContent().build();
  }

  /**
   * Tries to upvote a given comment.
   * 
   * @param id       unique comment id
   * @param username userame of user trying to upvote
   * @return 404 not found if invalid id. 400 bad request error with message if
   *         failed, otherwise 204 no content.
   */
  @PUT
  @Path("/upvote/{id}")
  public Response upvoteComment(@PathParam("id") int id, @QueryParam("username") String username) {
    if (username == null) {
      LOG.debug("Missing username param for upvote request, rejecting");
      return Response.status(Status.BAD_REQUEST).entity("username must be specified to upvote a comment").build();
    }
    int result = discussion.upvote(username, id);
    if (result == -1) {
      LOG.debug("Requested comment with id '" + id + "' does not exist, rejecting");
      return Response.status(Status.NOT_FOUND).entity("Comment with id '" + id + "' does not exist").build();
    }

    if (result == 0) {
      return Response.status(Status.BAD_REQUEST).entity("'" + username + "' already upvoted, rejected.").build();
    }

    return Response.noContent().build();
  }

  /**
   * Tries to downvote a given comment.
   * 
   * @param id       unique comment id
   * @param username userame of user trying to downvote
   * @return 400 bad request with message if failed, 404 not found if invalid id,
   *         otherwise 204 no content.
   */
  @PUT
  @Path("/downvote/{id}")
  public Response downvoteComment(@PathParam("id") int id, @QueryParam("username") String username) {
    if (username == null) {
      LOG.debug("Missing username param for downvote request, rejecting");
      return Response.status(Status.BAD_REQUEST).entity("username must be specified to downvote a comment").build();
    }
    int result = discussion.downvote(username, id);
    if (result == -1) {
      LOG.debug("Requested comment with id '" + id + "' does not exist, rejecting");
      return Response.status(Status.NOT_FOUND).entity("Comment with id '" + id + "' does not exist").build();
    }

    if (result == 0) {
      return Response.status(Status.BAD_REQUEST).entity("'" + username + "' already downvoted, rejected.").build();
    }

    return Response.noContent().build();
  }
}