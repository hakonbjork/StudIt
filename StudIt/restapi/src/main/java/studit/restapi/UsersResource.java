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
import studit.core.users.Hashing;
import studit.core.users.User;
import studit.core.users.Users;

public class UsersResource {

  private final Users users;
  private static final Logger LOG = LoggerFactory.getLogger(UsersResource.class);

  /**
   * Initalizes this UserResource with the main Users object.
   * 
   * @param users Users object obtained from StuditModel
   */
  public UsersResource(Users users) {
    this.users = users;
  }

  public void checkUsers() {
    if (this.users == null) {
      throw new IllegalArgumentException("No Users found");
    }
  }

  /**
   * Gets the active Users object.
   * 
   * @return the active Users object
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Users getCourseList() {
    checkUsers();
    LOG.debug("returning Users");
    return this.users;
  }

  /**
   * Returns User json object if found.
   * 
   * @param id unique id of user
   * @return Response with User json if found, otherwise 404 not found response.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") int id) {
    User user = users.getUserByID(id);
    if (user == null) {
      LOG.debug("User with the id '" + id + "' not found");
      return Response.status(Response.Status.NOT_FOUND).entity("user with the id '" + id + "' does not exist").build();
    }
    LOG.debug("Returning user with id '" + id + "'");
    return Response.ok(user, MediaType.APPLICATION_JSON).build();
  }

  /**
   * Removes a User from the database.
   * 
   * @param id unique User id
   * @return 404 not found if invalid id, 204 no content it succesfull.
   */
  @DELETE
  @Path("/remove/{id}")
  public Response removeUser(@PathParam("id") int id) {
    boolean removed = users.removeUser(id);
    if (!removed) {
      LOG.debug("Failed to removed user with id '" + id + "'");
      return Response.status(Response.Status.NOT_FOUND).entity("user with the id '" + id + "' does not exist").build();
    }
    LOG.debug("Succesfully to removed user with id '" + id + "'");
    return Response.noContent().build();
  }

  /**
   * Adds a new user to the database.
   * 
   * @param name     Full name of user
   * @param username username
   * @param mail     email
   * @param password password (will be hashed)
   * @return Response object containing user ID if succesfull (202 accepted),
   *         otherwise throws 400 bad request with error.
   */
  @POST
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addUser(@QueryParam("name") String name, @QueryParam("username") String username,
      @QueryParam("mail") String mail, @QueryParam("password") String password) {
    LOG.debug("Attempting to add user with following parameters: {name}: " + name + ", {username}: " + username
        + ", {mail}: " + mail + ", {password}:" + password);
    String[] result = users.addUser(name, username, mail, password);

    if (result[0] == null) {
      LOG.debug("Request failed with following error: " + result[1]);
      return Response.status(Status.BAD_REQUEST).entity(result).build();
    }
    LOG.debug("Successfully added new user to database");
    return Response.accepted(result).build();
  }

  /**
   * Process user login request.
   * 
   * @param username requested username
   * @param password corresponding password
   * @return 400 bad request if missing params, 401 unauthorized if invalid login,
   *         otherwise 200 ok.
   */
  @POST
  @Path("/authenticate")
  public Response authenticateLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
    if (username == null || password == null) {
      return Response.status(Status.BAD_REQUEST).entity("Request must include username and password parameters")
          .build();
    }

    boolean success = users.authenticateLogin(username, password);

    if (!success) {
      LOG.debug("Rejected following login: username: '" + username + "', password: '"
          + Hashing.hashPassword(password)[0] + "'");
      return Response.status(Response.Status.UNAUTHORIZED).entity("invalid login").build();
    }

    LOG.debug("Accepted following login: username: '" + username + "', password: '" + Hashing.hashPassword(password)[0]
        + "'");
    return Response.ok().build();
  }

  /**
   * Modifies the fields of a user.
   * 
   * @param id          unique user id
   * @param newMail     requested new mail
   * @param newPassword requested new password
   * @param newUsername requested new username
   * @return 400, Bad Request if more than one param is passed, 500, internal
   *         server error if the new field is invalid. 200 ok response containing
   *         sucess message
   */
  @PUT
  @Path("/modify/{id}")
  public Response changeUserField(@PathParam("id") int id, @QueryParam("newMail") String newMail,
      @QueryParam("newPassword") String newPassword, @QueryParam("newUsername") String newUsername) {

    if (newMail != null && newPassword != null || newPassword != null && newUsername != null) {
      return Response.status(Status.BAD_REQUEST).entity("Error, you can only change one field at a time").build();
    }

    String successMessage = "";

    if (newMail != null) {
      String mailResponse = users.changeMail(id, newMail);
      if (mailResponse != null) {
        LOG.debug("modify request failed with following error: " + mailResponse);
        return Response.serverError().entity(mailResponse).build();
      }
      successMessage = "Successfully changed mail to '" + newMail + "'";
    }

    if (newPassword != null) {
      String passwordResponse = users.changePassword(id, newPassword);
      if (passwordResponse != null) {
        LOG.debug("modify request failed with following error: " + passwordResponse);
        return Response.serverError().entity(passwordResponse).build();
      }
      successMessage = "Successfully changed password";
    }

    if (newUsername != null) {
      String usernameResponse = users.changeUsername(id, newUsername);
      if (usernameResponse != null) {
        LOG.debug("modify request failed with following error: " + usernameResponse);
        return Response.serverError().entity(usernameResponse).build();
      }
      successMessage = "Successfully changed username to '" + newUsername + "'";
    }

    LOG.debug("modify request succeeded with message: " + successMessage);
    return Response.ok().entity(successMessage).build();
  }
}