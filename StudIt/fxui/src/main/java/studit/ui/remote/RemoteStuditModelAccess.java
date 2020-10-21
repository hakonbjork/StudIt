package studit.ui.remote;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import studit.core.StuditModel;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;
import studit.json.StuditModule;
import studit.ui.Course;

public class RemoteStuditModelAccess {

  private static final String ENDPOINT_PATH = "http://localhost:8080/studit";
  private ObjectMapper objectMapper;

  public static void main(String[] args) throws ApiCallException {

    RemoteStuditModelAccess r = new RemoteStuditModelAccess();
    System.out.println(r.changeMail(2, "bruh@name.com")[1]);

  }

  public RemoteStuditModelAccess() {
    this.objectMapper = new ObjectMapper().registerModule(new StuditModule());
  }

  private URI buildUri(Map<String, String> params, String... paths) {
    StringBuilder uri = new StringBuilder(ENDPOINT_PATH);
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
      URI finalURI = new URI(uri.toString());
      return finalURI;
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HttpResponse<String> getResponse(HttpRequest request) {
    try {
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      return response;
    } catch (ConnectException e) {
      System.out.println("Could not establish connection to server");
      // TODO: update gui and tell that we've lost connection to the server.
    } catch (IOException | InterruptedException e1) {
      throw new RuntimeException(e1);
    }
    return null;
  }

  private HttpResponse<String> newGetRequest(Map<String, String> params, String... paths) throws ApiCallException {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").GET().build();

    try {
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      return response;
    } catch (ConnectException e) {
      throw new ApiCallException("Could not establish connection to the server.");
    } catch (IOException | InterruptedException e1) {
      throw new RuntimeException(e1);
    }
  }

  private HttpResponse<String> newPostRequest(String json, Map<String, String> params, String... paths) {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json")
        .POST(json != null ? BodyPublishers.ofString(json) : BodyPublishers.noBody()).build();

    return getResponse(request);
  }

  private HttpResponse<String> newPutRequest(String json, Map<String, String> params, String... paths) {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json")
        .PUT(json != null ? BodyPublishers.ofString(json) : BodyPublishers.noBody()).build();

    return getResponse(request);
  }

  private HttpResponse<String> newDeleteRequest(Map<String, String> params, String... paths) {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").DELETE().build();

    return getResponse(request);
  }

  /**
   * Gets the entire StuditModel object of the server database.
   * 
   * @return StuditModel if server is configured properly, else null
   * @throws ApiCallException
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
   * @throws ApiCallException
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
   * Gets the Users object from the server
   * 
   * @return Users object if server is configured properly, othwerise null
   * @throws ApiCallException
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
   * Gets User object by given ID
   * 
   * @param id unique user ID
   * @return User object if user with id exists, else ApiCallException is thrown
   * @throws ApiCallException
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
   * Removes user from server database
   * 
   * @param id unique user ID
   * @return true if user was removed, false if unknown response from server,
   *         ApiCallException if user not found
   * @throws ApiCallException
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
   * Adds a new user to the server database
   * 
   * @param name     full name
   * @param username unique username
   * @param mail     mail address
   * @param password password (will be hashed)
   * @return String[] of the result. String[0] contains the status code (can be
   *         safely cast to int). 0 = ok, -1 = missing fields, -2 = username is
   *         not unique, -3 = password is not valid / not strong enough. String[1]
   *         contains the error message as to why the request failed, e.g "the
   *         password is too short". Null if new user was succesfully added
   * @throws ApiCallException
   */
  public String[] addUser(String name, String username, String mail, String password) throws ApiCallException {
    HttpResponse<String> response = newPostRequest(null,
        Map.of("name", name, "username", username, "mail", mail, "password", password), "users", "add");

    String[] result;
    try {
      result = objectMapper.readValue(response.body(), String[].class);
      if (response.statusCode() == Status.SERVER_ERROR.get()) {
        return new String[] { result[2], result[1] };
      }
      return new String[] { result[2], null };
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new ApiCallException("Error reading response, check API code for errors");
    }
  }

  /**
   * Authenticate login request.
   * 
   * @param username username to check
   * @param password password to check
   * @return true if valid login, else false.
   * @throws ApiCallException
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
   * Change password of the user
   * 
   * @param id          unique user id
   * @param newPassword new password
   * @return String[] containg result. String[0] is null if request was
   *         succesfull, -1 otherwise. String[1] contains error/success message
   * @throws ApiCallException
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
   * Change username of user
   * 
   * @param id          unique user id
   * @param newUsername new username
   * @return String[] containg result. String[0] is null if request was
   *         succesfull, -1 otherwise. String[1] contains error/success message
   * @throws ApiCallException
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
   * Change mail of user
   * 
   * @param id          unique user id
   * @param newUsername new username
   * @return String[] containg result. String[0] is null if request was
   *         succesfull, -1 otherwise. String[1] contains error/success message
   * @throws ApiCallException
   */
  public String[] changeMail(int id, String newMail) throws ApiCallException {
    HttpResponse<String> response = newPutRequest(null, Map.of("newMail", newMail), "users", "modify",
        String.valueOf(id));

    int code = response.statusCode();

    System.out.println(code);
    if (code == Status.BAD_REQUEST.get()) {
      throw new ApiCallException("Unknown api error occured");
    } else if (code == Status.SERVER_ERROR.get()) {
      return new String[] { "-1", response.body() };
    }
    return new String[] { null, response.body() };
  }

  /**
   * Get the Course object with the requested fagkode
   * @param fagkode fagkode of the course
   * @return Course if a course with the fagkode exists, otherwise throws ApiCallException.
   * @throws ApiCallException
   */
  public Course getCourseByFagkode(String fagkode) throws ApiCallException {
    HttpResponse<String> response = newGetRequest(null, "users", fagkode);

    if (response.statusCode() == Status.NOT_FOUND.get()) {
      throw new ApiCallException(response.body());
    }
    try {
      return objectMapper.readValue(response.body(), Course.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Discussion getDiscussion(String fagkode) throws ApiCallException {
    return null;
  }

}

enum Status {
  OK(200), ACCEPTED(202), NO_CONTENT(204), BAD_REQUEST(400), UNAUTHORIZED(401), NOT_FOUND(404), SERVER_ERROR(500);

  private int code;

  private Status(int code) {
    this.code = code;
  }

  public int get() {
    return this.code;
  }
}