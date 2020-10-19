package studit.restapi;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studit.core.users.Users;

public class UsersResource {

  private final Users users;
  private static final Logger LOG = LoggerFactory.getLogger(UsersResource.class);
  
  /**
   * Initalizes this UserResource with the main Users object
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
   * Gets the active Users object
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
}