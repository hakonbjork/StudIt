package studit.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map.Entry;

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

  protected final boolean DEBUG = false;
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
  public void getCourseByFagkode() {
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
    assertEquals(comment1.getDato(), comment2.getDato());
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