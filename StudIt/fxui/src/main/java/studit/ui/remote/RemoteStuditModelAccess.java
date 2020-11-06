package studit.ui.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Map.Entry;
import studit.core.StuditModel;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;
import studit.json.StuditModule;

public class RemoteStuditModelAccess {

  private static String DEFAULT_PATH = "http://localhost:8080/studit";

  private String endpointPath;
  protected ObjectMapper objectMapper;
  private boolean test = false;

  public RemoteStuditModelAccess() {
    this(false);
  }

  public RemoteStuditModelAccess(boolean test) {
    endpointPath = !test ? DEFAULT_PATH : "http://localhost:9998/studit";
    this.objectMapper = new ObjectMapper().registerModule(new StuditModule());
    this.test = test;
  }

  private URI buildUri(Map<String, String> params, String... paths) {
    StringBuilder uri = new StringBuilder(endpointPath);
    for (String path : paths) {
      uri.append("/");
      uri.append(path);
    }

    if (params != null) {
      uri.append("?");
      for (Entry<String, String> param : params.entrySet()) {
        uri.append(param.getKey());
        uri.append("=");
        uri.append(param.getValue());
        uri.append("&");
      }
      // Remove last and
      uri.deleteCharAt(uri.length() - 1);
    }

    try {
      URI finalUri = new URI(uri.toString().replace(" ", "%20"));
      return finalUri;
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HttpResponse<String> getResponse(HttpRequest request) throws ApiCallException {
    try {
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      return response;
    } catch (ConnectException e) {
      throw new ApiCallException("Could not establish connection to server");
    } catch (IOException | InterruptedException e1) {
      throw new RuntimeException(e1);
    }
  }

  private HttpResponse<String> newGetRequest(Map<String, String> params, String... paths) throws ApiCallException {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").GET().build();

    return getResponse(request);
  }

  private HttpResponse<String> newPostRequest(String json, Map<String, String> params, String... paths)
      throws ApiCallException {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json")
        .POST(json != null ? BodyPublishers.ofString(json) : BodyPublishers.noBody()).build();

    return getResponse(request);
  }

  private HttpResponse<String> newPutRequest(String json, Map<String, String> params, String... paths)
      throws ApiCallException {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json")
        .PUT(json != null ? BodyPublishers.ofString(json) : BodyPublishers.noBody()).build();

    return getResponse(request);
  }

  private HttpResponse<String> newDeleteRequest(Map<String, String> params, String... paths) throws ApiCallException {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").DELETE().build();

    return getResponse(request);
  }

  /**
   * Ping the server.
   * 
   * @return true if succesful connection, false otherwise
   */
  public boolean ping() {
    try {
      HttpResponse<String> response = newGetRequest(null, "ping");
      if (response.statusCode() != Status.OK.get()) {
        System.out.println("Failed with status code: " + response.statusCode());
        return false;
      }
      return true;
    } catch (ApiCallException e) {
      if (!test) {
        //e.printStackTrace();
        System.out.println("Could not establish connection to server.");
      }
      return false;
    }

  }

  /**
   * Gets the entire StuditModel object of the server database.
   * 
   * @return StuditModel if server is configured properly, else null
   * @throws ApiCallException if server is configured improperly
   */
  public StuditModel getStuditModel() throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "");
    try {
      return objectMapper.readValue(response.body(), StuditModel.class);
    } catch (IOException e) {
      System.out.println("Error occured reading StuditModel response");
      return null;
    }
  }

  /**
   * Gets the CourseList object.
   * 
   * @return Courselist object if server is configured properly, othwerise null
   * @throws ApiCallException if server is configured imporperly
   */
  public CourseList getCourseList() throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "courses");
    try {
      return objectMapper.readValue(response.body(), CourseList.class);
    } catch (IOException e) {
      System.out.println("Error occured reading CourseList response");
      return null;
    }
  }

  /**
   * Gets the Users object from the server.
   * 
   * @return Users object if server is configured properly, othwerise null
   * @throws ApiCallException if server is configured improperly
   */
  public Users getUsers() throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "users");
    try {
      return objectMapper.readValue(response.body(), Users.class);
    } catch (IOException e) {
      System.out.println("Error occured reading Users response");
      return null;
    }
  }

  /**
   * Gets User object by given ID.
   * 
   * @param id unique user ID
   * @return User object if user with id exists, else ApiCallException is thrown
   * @throws ApiCallException if user with the id does not exist.
   */
  public User getUserByID(int id) throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "users", String.valueOf(id));
    if (response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    }
    try {
      return objectMapper.readValue(response.body(), User.class);
    } catch (IOException e) {
      System.out.println("Error occured reading Users response");
      return null;
    }
  }

  /**
   * Removes user from server database.
   * 
   * @param id unique user ID
   * @return true if user was removed, false if unknown response from server,
   *         ApiCallException if user not found
   * @throws ApiCallException if user with the given id does not exist
   */
  public boolean removeUserByID(int id) throws ApiCallException {
    HttpResponse<String> response = newDeleteRequest(null, "users", "remove", String.valueOf(id));

    int code = response.statusCode();

    if (code == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    } else if (code == Status.NO_CONTENT.get()) {
      return true;
    }
    return false;
  }

  /**
   * Adds a new user to the server database.
   * 
   * @param name     full name
   * @param username unique username
   * @param mail     mail address
   * @param password password (will be hashed)
   * @return String[] of the result. String[0] contains the status code (can be
   *         safely cast to int). 0 = ok, -1 = missing fields, -2 = username is
   *         not unique, -3 = password is not valid / not strong enough. -4 if
   *         invalid email. String[1] contains the error message as to why the
   *         request failed, e.g "the password is too short". Null if new user was
   *         succesfully added
   * @throws ApiCallException Unknown cause
   */
  public String[] addUser(String name, String username, String mail, String password) throws ApiCallException {
    try {
      HttpResponse<String> response = newPostRequest(null,
          Map.of("name", name, "username", username, "mail", mail, "password", password), "users", "add");

      String[] result;
      try {
        result = objectMapper.readValue(response.body(), String[].class);
        if (response.statusCode() == Status.BAD_REQUEST.get()) {
          return new String[] { result[2], result[1] };
        }
        return new String[] { result[2], null };
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        throw new ApiCallException("Error reading response, check API code for errors");
      }
    } catch (NullPointerException e) {
      return new String[] { "-1", "Parameters passed must not be null" };
    }
  }

  /**
   * Authenticate login request.
   * 
   * @param username username to check
   * @param password password to check
   * @return true if valid login, else false.
   * @throws ApiCallException Unknown cause.
   */
  public boolean authenticateLogin(String username, String password) throws ApiCallException {
    HttpResponse<String> response = newPostRequest(null, Map.of("username", username, "password", password), "users",
        "authenticate");

    int code = response.statusCode();

    if (code == Status.BAD_REQUEST.get()) {
      throw new ApiCallException(response.body());
    } else if (code == Status.UNAUTHORIZED.get()) {
      return false;
    }
    return true;
  }

  /**
   * Change password of the user.
   * 
   * @param id          unique user id
   * @param newPassword new password
   * @return String[] containg result. String[0] is null if request was
   *         succesfull, -1 otherwise. String[1] contains error/success message
   * @throws ApiCallException unknown cause.
   */
  public String[] changePassword(int id, String newPassword) throws ApiCallException {
    HttpResponse<String> response = newPutRequest(null, Map.of("newPassword", newPassword), "users", "modify",
        String.valueOf(id));

    int code = response.statusCode();

    if (code == Status.BAD_REQUEST.get()) {
      throw new ApiCallException("Unknown api error occured");
    } else if (code == Status.SERVER_ERROR.get()) {
      return new String[] { "-1", response.body() };
    }
    return new String[] { null, response.body() };

  }

  /**
   * Change username of user.
   * 
   * @param id          unique user id
   * @param newUsername new username
   * @return String[] containg result. String[0] is null if request was
   *         succesfull, -1 otherwise. String[1] contains error/success message
   * @throws ApiCallException unknown cause
   */
  public String[] changeUsername(int id, String newUsername) throws ApiCallException {
    HttpResponse<String> response = newPutRequest(null, Map.of("newUsername", newUsername), "users", "modify",
        String.valueOf(id));

    int code = response.statusCode();

    if (code == Status.BAD_REQUEST.get()) {
      throw new ApiCallException("Unknown api error occured");
    } else if (code == Status.SERVER_ERROR.get()) {
      return new String[] { "-1", response.body() };
    }
    return new String[] { null, response.body() };
  }

  /**
   * Change mail of user.
   * 
   * @param id      unique user id
   * @param newMail new mail
   * @return String[] containg result. String[0] is null if request was
   *         succesfull, -1 otherwise. String[1] contains error/success message
   * @throws ApiCallException unkown cause
   */
  public String[] changeMail(int id, String newMail) throws ApiCallException {
    HttpResponse<String> response = newPutRequest(null, Map.of("newMail", newMail), "users", "modify",
        String.valueOf(id));

    int code = response.statusCode();

    if (code == Status.BAD_REQUEST.get()) {
      throw new ApiCallException("Unknown api error occured");
    } else if (code == Status.SERVER_ERROR.get()) {
      return new String[] { "-1", response.body() };
    }
    return new String[] { null, response.body() };
  }

  /**
   * Get the Course object with the requested fagkode.
   * 
   * @param fagkode fagkode of the course
   * @return Course if a course with the fagkode exists, otherwise throws
   *         ApiCallException.
   * @throws ApiCallException if the course with the fagkode does not exist
   */
  public CourseItem getCourseByFagkode(String fagkode) throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "courses", fagkode);

    if (response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    }

    try {
      return objectMapper.readValue(response.body(), CourseItem.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get Discussion object by given fagkode.
   * 
   * @param fagkode fagkode of the requested course
   * @return Discussion if fagkode exits, otherwise null or ApiCallException is
   *         thrown
   * @throws ApiCallException if the course with the fagkode does not exist
   */
  public Discussion getDiscussion(String fagkode) throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "courses", fagkode, "discussion");

    try {
      return objectMapper.readValue(response.body(), Discussion.class);
    } catch (JsonProcessingException e) {
      throw new ApiCallException("There does not exist a course with fagkode" + fagkode);
    }

  }

  /**
   * Add a comment to a discussion.
   * 
   * @param fagkode  fagkode of the course
   * @param username username who wrote the comment (stored locally)
   * @param comment  comment to send
   * @return unique id of the new comment if request is successfull, otherwise
   *         throws ApiCallException
   * @throws ApiCallException unknown cause.
   */
  public int addCommentToDiscussion(String fagkode, String username, String comment) throws ApiCallException {
    try {
      HttpResponse<String> response = newPostRequest(null, Map.of("username", username, "comment", comment), "courses",
          fagkode, "discussion", "add");

      if (response.statusCode() == Status.BAD_REQUEST.get()) {
        throw new ApiCallException(response.body());
      } else if (response.statusCode() == Status.FORBIDDEN.get() || response.statusCode() == Status.NOT_FOUND.get()) {
        throw new ApiCallException("Course with fagkode '" + fagkode + "' does not exist'");
      }

      return Integer.parseInt(response.body());
    } catch (NullPointerException e) {
      throw new ApiCallException("One of the parameters passed was null");
    }
  }

  /**
   * Get comment by id and fagkode.
   * 
   * @param fagkode Course fagkode.
   * @param id      unique id of comment.
   * @return Comment if it exists, throws ApiCallException if not.
   * @throws ApiCallException if comment does not exist, or there is a
   *                          json-parsing error.
   */
  public Comment getCommentById(String fagkode, int id) throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "courses", fagkode, "discussion", "comment",
        String.valueOf(id));

    if (response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException("Error -> comment with id '" + id + "' not found'");
    }

    try {
      return objectMapper.readValue(response.body(), Comment.class);
    } catch (JsonProcessingException e) {
      throw new ApiCallException(e.getMessage());
    }
  }

  /**
   * Delete a comment from a discussion.
   * 
   * @param fagkode fagkode of the requested discussion
   * @param id      unique id of the comment to delete
   * @return true if successful, throws ApiCallException if id does not exist
   * @throws ApiCallException if the comment with the id does not exist.
   */
  public boolean deleteCommentByID(String fagkode, int id) throws ApiCallException {
    HttpResponse<String> response = newDeleteRequest(null, "courses", fagkode, "discussion", "remove",
        String.valueOf(id));
    if (response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    }
    return true;
  }

  /**
   * Upvotes the given comment.
   * 
   * @param fagkode  fagkode of the requested discsussion
   * @param username username that wants to upvote
   * @param id       unique id of the comment the user wants to upvote
   * @return true if successful, otherwise throws ApiCallException (also thrown if
   *         the user has upvoted the comment before)
   * @throws ApiCallException if the comment does not exist, or it has been
   *                          upvoted before.
   */
  public boolean upvoteCommentByID(String fagkode, String username, int id) throws ApiCallException {
    HttpResponse<String> response = newPutRequest(null, Map.of("username", username), "courses", fagkode, "discussion",
        "upvote", String.valueOf(id));

    if (response.statusCode() == Status.BAD_REQUEST.get() || response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    }

    return true;
  }

  /**
   * Upvotes the given comment.
   * 
   * @param fagkode  fagkode of the requested discsussion
   * @param username username that wants to downvote
   * @param id       unique id of the comment the user wants to downvote
   * @return true if successful, otherwise throws ApiCallException (also thrown if
   *         the user has downvoted the comment before)
   * @throws ApiCallException if the comment does not exist, or it has been
   *                          downvoted before.
   */
  public boolean downvoteCommentByID(String fagkode, String username, int id) throws ApiCallException {
    HttpResponse<String> response = newPutRequest(null, Map.of("username", username), "courses", fagkode, "discussion",
        "downvote", String.valueOf(id));

    if (response.statusCode() == Status.BAD_REQUEST.get() || response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    }

    return true;
  }

  /**
   * This method is used for testing purposes only, to test handling of requests
   * with invalid pathing, in case they should occur.
   */
  public void setTestEndpointPath() {
    this.endpointPath = "http://localhost:9998/studet";
  }

  /**
   * This method is used for testing purposes only, to test handling of requests
   * with invalid pathing, in case they should occur.
   * 
   */
  public void resetPath() {
    this.endpointPath = "http://localhost:9998/studit";
  }

  /**
   * Set test param.
   * 
   * @param test set to true to disable error print.
   */
  public void setTest(boolean test) {
    this.test = test;
  }

  enum Status {
    OK(200), ACCEPTED(202), NO_CONTENT(204), BAD_REQUEST(400), UNAUTHORIZED(401), NOT_FOUND(404), FORBIDDEN(405),
    SERVER_ERROR(500);

    private int code;

    private Status(int code) {
      this.code = code;
    }

    public int get() {
      return this.code;
    }
  }

}
