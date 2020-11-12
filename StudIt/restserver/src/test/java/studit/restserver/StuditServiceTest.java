package studit.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;
import studit.restapi.StuditService;

public class StuditServiceTest extends JerseyTest {

  protected final boolean DEBUG = true;
  protected final String PATH = StuditService.STUDIT_SERVICE_PATH;
  protected ObjectMapper mapper;
  protected StuditModel defaultModel = DefaultGenerator
      .writeDefaultDataToDb("src/test/resources/studit/restserver/defaultdb.json");

  @Override
  protected StuditConfig configure() {
    final StuditConfig config = new StuditConfig("src/test/resources/studit/restserver/defaultdb.json");
    if (DEBUG) {
      enable(TestProperties.LOG_TRAFFIC);
      enable(TestProperties.DUMP_ENTITY);
      config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
    }
    return config;
  }

  @Override
  protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
    return new GrizzlyTestContainerFactory();
  }

  @Override
  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    mapper = new StuditModuleObjectMapperProvider().getContext(getClass());
  }

  @AfterEach
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testPing() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/ping").request().get();
    assertEquals(200, getResponse.getStatus());
  }

  @Test
  public void testGetStuditModel() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      StuditModel studitModel = mapper.readValue(getResponse.readEntity(String.class), StuditModel.class);
      compareStuditModels(defaultModel, studitModel);
    } catch (JsonProcessingException e) {
      fail(e);
    }

  }

  @Test
  public void testGetCourses() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      CourseList courses = mapper.readValue(getResponse.readEntity(String.class), CourseList.class);
      compareCourseLists(defaultModel.getCourseList(), courses);
    } catch (JsonProcessingException e) {
      fail(e);
    }
  }

  @Test
  public void testGetUsers() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      Users users = mapper.readValue(getResponse.readEntity(String.class), Users.class);
      compareUsers(defaultModel.getUsers(), users);
    } catch (JsonProcessingException e) {
      fail(e);
    }
  }

  @Test
  public void testGetCourseByFagkode() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      CourseItem courseItem = mapper.readValue(getResponse.readEntity(String.class), CourseItem.class);
      compareCourseItem(defaultModel.getCourseList().getCourseByFagkode("TMA4140"), courseItem);
    } catch (JsonProcessingException e) {
      fail(e);
    }

    Response getResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA3")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(404, getResponse2.getStatus());

  }

  @Test
  public void testGetDiscussion() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      Discussion discussion = mapper.readValue(getResponse.readEntity(String.class), Discussion.class);
      compareDiscussion(discussion, defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon());
    } catch (JsonProcessingException e) {
      fail(e);
    }

    Response getResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA3/discussion")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals("", getResponse2.readEntity(String.class));

  }

  @Test
  public void testAddComment() {
    Response postResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/add")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(null);
    assertEquals(400, postResponse.getStatus());

    Discussion testDiscussion = defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon();
    Response postResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/add")
        .queryParam("username", "test").queryParam("comment", "test2").request().post(Entity.json(""));
    testDiscussion.addComment("test", "test2");

    assertEquals(200, postResponse2.getStatus());

    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      Discussion discussion = mapper.readValue(getResponse.readEntity(String.class), Discussion.class);
      compareDiscussion(testDiscussion, discussion);
    } catch (JsonProcessingException e) {
      fail(e);
    }

  }

  @Test
  public void testGetCommentById() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/comment/-1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(404, getResponse.getStatus());

    Response getResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/comment/0")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse2.getStatus());

    try {
      Comment comment = mapper.readValue(getResponse2.readEntity(String.class), Comment.class);

      Comment testComment = defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().getCommentByID(0);
      compareComment(comment, testComment);

    } catch (JsonProcessingException e) {
      fail(e);
    }
  }

  @Test
  public void testDeleteComment() {
    Response deleteResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/remove/6")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").delete();
    assertEquals(404, deleteResponse.getStatus());

    Response deleteResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/remove/1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").delete();
    assertEquals(204, deleteResponse2.getStatus());

    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {
      Discussion discussion = mapper.readValue(getResponse.readEntity(String.class), Discussion.class);

      Discussion testDiscussion = defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon();
      testDiscussion.removeComment(1);
      compareDiscussion(testDiscussion, discussion);

    } catch (JsonProcessingException e) {
      fail(e);
    }

  }

  @Test
  public void testUpvoteDownvoteComment() {
    Response putResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/uvpote/6")
        .queryParam("username", "Bobby")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(404, putResponse.getStatus());

    Response putResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/downvote/6")
        .queryParam("username", "Dingo")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(404, putResponse2.getStatus());

    Response putResponse3 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/upvote/1")
        .queryParam("username", "Bobby")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(204, putResponse3.getStatus());

    Response putResponse4 = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion/downvote/0")
        .queryParam("username", "Dingo")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(204, putResponse4.getStatus());

    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/courses/TMA4140/discussion")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {

      Discussion testDiscussion = defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon();
      testDiscussion.upvote("Bobby", 1);
      testDiscussion.downvote("Dingo", 0);

      Discussion discussion = mapper.readValue(getResponse.readEntity(String.class), Discussion.class);
      compareDiscussion(testDiscussion, discussion);

    } catch (JsonProcessingException e) {
      fail(e);
    }

  }

  @Test
  public void testRemoveUsers() {
    Response deleteResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users/remove/6")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").delete();
    assertEquals(404, deleteResponse.getStatus());

    Response deleteResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/users/remove/1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").delete();
    assertEquals(204, deleteResponse2.getStatus());

    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {

      Users testUsers = defaultModel.getUsers();
      testUsers.removeUser(1);

      Users users = mapper.readValue(getResponse.readEntity(String.class), Users.class);
      compareUsers(testUsers, users);

    } catch (JsonProcessingException e) {
      fail(e);
    }

  }

  @Test
  public void testAddUser() {
    Response postResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users/add")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(400, postResponse.getStatus());

    Response postResponse1 = target(StuditService.STUDIT_SERVICE_PATH + "/users/add").queryParam("username", "name1")
        .queryParam("password", "1").queryParam("mail", "bruh").queryParam("name", "Janne")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(400, postResponse1.getStatus());

    Response postResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/users/add").queryParam("username", "name1")
        .queryParam("password", "1").queryParam("mail", "bruh@mail.com").queryParam("name", "Janne")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(400, postResponse2.getStatus());

    Response postResponse3 = target(StuditService.STUDIT_SERVICE_PATH + "/users/add").queryParam("username", "name1")
        .queryParam("password", "password123").queryParam("mail", "bruh@mail.com").queryParam("name", "Janne")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(202, postResponse3.getStatus());

    Response postResponse4 = target(StuditService.STUDIT_SERVICE_PATH + "/users/add").queryParam("username", "name1")
        .queryParam("password", "password123").queryParam("mail", "bruh@mail.com").queryParam("name", "Janne")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(400, postResponse4.getStatus());

    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    try {

      Users testUsers = defaultModel.getUsers();
      testUsers.addUser("Janne", "name1", "bruh@mail.com", "password123");

      Users users = mapper.readValue(getResponse.readEntity(String.class), Users.class);
      compareUsers(testUsers, users);

    } catch (JsonProcessingException e) {
      fail(e);
    }
  }

  @Test
  public void testAuthenticateLogin() {
    Response postResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users/authenticate")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(400, postResponse.getStatus());

    Response postResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/users/authenticate")
        .queryParam("username", "foofoo").queryParam("password", "foofoo2")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(401, postResponse2.getStatus());

    Response postResponse3 = target(StuditService.STUDIT_SERVICE_PATH + "/users/authenticate")
        .queryParam("username", "IdaErBest").queryParam("password", "pomeranian123")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").post(Entity.json(""));
    assertEquals(200, postResponse3.getStatus());
  }

  @Test
  public void testGetUserByUsername() {
    Response getResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users/username/Berte92")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());

    Response getResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/users/username/berte92")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(404, getResponse2.getStatus());

    try {

      User user = defaultModel.getUsers().getUserByUsername("Berte92");
      User testUser = mapper.readValue(getResponse.readEntity(String.class), User.class);

      compareUser(user, testUser);

    } catch (JsonProcessingException e) {
      fail(e);
    }

  }

  @Test
  public void testModifyUser() {
    Response postResponse = target(StuditService.STUDIT_SERVICE_PATH + "/users/modify/1")
        .queryParam("newUsername", "brother").queryParam("newPassword", "pass")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(400, postResponse.getStatus());

    Response postResponse2 = target(StuditService.STUDIT_SERVICE_PATH + "/users/modify/8")
        .queryParam("newUsername", "brother")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(500, postResponse2.getStatus());

    Response postResponse3 = target(StuditService.STUDIT_SERVICE_PATH + "/users/modify/1")
        .queryParam("newUsername", "Ida")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(200, postResponse3.getStatus());

    Response postResponse4 = target(StuditService.STUDIT_SERVICE_PATH + "/users/modify/1")
        .queryParam("newPassword", "123")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(500, postResponse4.getStatus());

    Response postResponse5 = target(StuditService.STUDIT_SERVICE_PATH + "/users/modify/1")
        .queryParam("newPassword", "123strongpassword")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").put(Entity.json(""));
    assertEquals(200, postResponse5.getStatus());
  }

  /**
   * Compares two StuditModels, throws assertionError if they are different.
   * 
   * @param studitModel1 Correct Model
   * @param studitModel2 Model to compare against
   */
  public static void compareStuditModels(StuditModel studitModel1, StuditModel studitModel2) {
    if (studitModel1 == null && studitModel2 == null) {
      return;
    }

    Users users1 = studitModel1.getUsers();
    Users users2 = studitModel2.getUsers();

    compareUsers(users1, users2);

    CourseList courseList1 = studitModel1.getCourseList();
    CourseList courseList2 = studitModel2.getCourseList();

    compareCourseLists(courseList1, courseList2);
  }

  /**
   * Compares two Users objects, throws AssertionError if they are different.
   * 
   * @param users1 Correct Users object
   * @param users2 Users object to compare against
   */
  public static void compareUsers(Users users1, Users users2) {
    if (users1 == null && users2 == null) {
      return;
    }

    assertEquals(users1.getPrevAssignedID(), users2.getPrevAssignedID());
    for (Entry<Integer, User> entry : users1.getUsers().entrySet()) {
      User userToCheck = users2.getUserByID(entry.getKey());
      assertNotNull(userToCheck);
      compareUser(entry.getValue(), userToCheck);
    }
  }

  /**
   * Compares two User objects, throws AssertionError if they are different.
   * 
   * @param user1 Correct User object
   * @param user2 User object to compare against.
   */
  public static void compareUser(User user1, User user2) {
    if (user1 == null && user2 == null) {
      return;
    }

    assertEquals(user1.getClass(), user2.getClass());
    assertEquals(user1.getMail(), user2.getMail());
    assertEquals(user1.getName(), user2.getName());
    assertEquals(user1.getPassword(), user2.getPassword());
    assertEquals(user1.getUniqueID(), user2.getUniqueID());
    assertEquals(user1.getUsername(), user2.getUsername());
  }

  /**
   * Compares two CourseLits. Throws AssertionError if they are different.
   * 
   * @param courseList1 Correct CourseList
   * @param courseList2 CourseList to compare against.
   */
  public static void compareCourseLists(CourseList courseList1, CourseList courseList2) {
    if (courseList1 == null && courseList2 == null) {
      return;
    }

    List<CourseItem> list1 = courseList1.getCourseItems();
    List<CourseItem> list2 = courseList1.getCourseItems();

    assertEquals(list1.size(), list2.size());

    for (int i = 0; i < list1.size(); i++) {
      compareCourseItem(list1.get(i), list2.get(i));
    }
  }

  /**
   * Compares two CourseItems. Throws AssertionError if they are different.
   * 
   * @param courseItem1 Correct CourseItem
   * @param courseItem2 CourseItem to compare against
   */
  public static void compareCourseItem(CourseItem courseItem1, CourseItem courseItem2) {
    if (courseItem1 == null && courseItem2 == null) {
      return;
    }

    assertEquals(courseItem1.getAverageVurdering(), courseItem2.getAverageVurdering());
    assertEquals(courseItem1.getAnbefaltLitteratur(), courseItem2.getAnbefaltLitteratur());
    assertEquals(courseItem1.getEksamensdato(), courseItem2.getEksamensdato());
    assertEquals(courseItem1.getFagkode(), courseItem2.getFagkode());
    assertEquals(courseItem1.getFagnavn(), courseItem2.getFagnavn());
    assertEquals(courseItem1.getHjelpemidler(), courseItem2.getHjelpemidler());
    assertEquals(courseItem1.getInformasjon(), courseItem2.getInformasjon());
    assertEquals(courseItem1.getPensumlitteratur(), courseItem2.getPensumlitteratur());
    assertEquals(courseItem1.getTips(), courseItem2.getTips());
    assertEquals(courseItem1.getVurderingsform(), courseItem2.getVurderingsform());

    List<Integer> vurdering1 = courseItem1.getVurderinger();
    List<Integer> vurdering2 = courseItem2.getVurderinger();
    assertEquals(vurdering1.size(), vurdering2.size());

    for (int i = 0; i < vurdering1.size(); i++) {
      assertEquals(vurdering1.get(i), vurdering2.get(i));
    }

    compareDiscussion(courseItem1.getDiskusjon(), courseItem2.getDiskusjon());
  }

  /**
   * Compares two Discussion objects. Throws AssertionError if they are different.
   * 
   * @param discussion1 Correct Discussion object.
   * @param discussion2 Discussion to compare against.
   */
  private static void compareDiscussion(Discussion discussion1, Discussion discussion2) {
    if (discussion1 == null && discussion2 == null) {
      return;
    }

    assertEquals(discussion1.getPrevAssignedID(), discussion2.getPrevAssignedID());

    for (Entry<Integer, Comment> entry : discussion1.getComments().entrySet()) {

      Comment commentToCheck = discussion2.getCommentByID(entry.getKey());

      assertNotNull(commentToCheck);
      compareComment(entry.getValue(), commentToCheck);
    }
  }

  /**
   * Compares two Comments. Throws AssertionError if they are different.
   * 
   * @param comment1 Correct Comment.
   * @param comment2 Comment to compare against.
   */
  private static void compareComment(Comment comment1, Comment comment2) {
    if (comment1 == null && comment2 == null) {
      return;
    }

    assertEquals(comment1.getBrukernavn(), comment2.getBrukernavn());
    assertEquals(comment1.getKommentar(), comment2.getKommentar());
    assertEquals(comment1.getUniqueID(), comment2.getUniqueID());
    assertEquals(comment1.getUpvotes(), comment2.getUpvotes());
    assertEquals(comment1.getDownvotes(), comment2.getDownvotes());

    List<String> upvoters1 = comment1.getUpvoters();
    List<String> upvoters2 = comment2.getUpvoters();

    assertEquals(upvoters1.size(), upvoters2.size());
    for (int i = 0; i < upvoters1.size(); i++) {
      assertEquals(upvoters1.get(i), upvoters2.get(i));
    }

    List<String> downvoters1 = comment1.getDownvoters();
    List<String> downvoters2 = comment2.getDownvoters();

    assertEquals(downvoters1.size(), downvoters2.size());

    for (int i = 0; i < downvoters1.size(); i++) {
      assertEquals(downvoters1.get(i), downvoters2.get(i));
    }
  }

}