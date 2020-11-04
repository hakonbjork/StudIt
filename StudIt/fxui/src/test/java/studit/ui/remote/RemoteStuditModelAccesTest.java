package studit.ui.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;
import studit.ui.TestServlet;

public class RemoteStuditModelAccesTest {

  /**
   * To prevent multiple server restarts, we only use the @Test annotation when a
   * strict server restart is required (every time a new Test instance is created
   * the server is restarted)
   */

  private TestServlet testServlet = new TestServlet();
  private StuditModel defaultModel = testServlet.getDefaultModel();
  private RemoteStuditModelAccess remoteModel = new RemoteStuditModelAccess(true);

  @Test
  public void testPing() throws Exception {
    assertTrue(remoteModel.ping());
    remoteModel.setTestEndpointPath();
    assertFalse(remoteModel.ping());
    testServlet.getJerseyTest().tearDown();
    assertFalse(remoteModel.ping());

    remoteModel = new RemoteStuditModelAccess();
    remoteModel.setTest(true);
    assertFalse(remoteModel.ping());
  }

  /**
   * The bulk of the functionallity will be tested here (prevents unnessecary
   * server restartss)
   * 
   * @throws Exception ignore.
   */
  @Test
  public void tests() throws Exception {
    testGetStuditModel();
    testGetUsers();
    testGetCourseList();
    testGetUserById();
    testAddRemoveUsers();
    testAuthenticateLogin();
    testModifyFields();
    testGetCourseByFagkode();
    testGetDiscussionByFagkode();
    testGetCommentById();
    testAddCommentToDiscussion();
    testDeleteCommentById();
    testUpvoteCommentById();
    testDownvoteCommentById();
  }

  public void testGetStuditModel() throws ApiCallException {
    compareStuditModels(defaultModel, remoteModel.getStuditModel());
    remoteModel.setTestEndpointPath();
    assertNull(remoteModel.getStuditModel());
    remoteModel.resetPath();
  }

  public void testGetUsers() throws ApiCallException {
    compareUsers(defaultModel.getUsers(), remoteModel.getUsers());
    remoteModel.setTestEndpointPath();
    assertNull(remoteModel.getUsers());
    remoteModel.resetPath();
  }

  public void testGetCourseList() throws ApiCallException {
    compareCourseLists(defaultModel.getCourseList(), remoteModel.getCourseList());
    remoteModel.setTestEndpointPath();
    assertNull(remoteModel.getCourseList());
    remoteModel.resetPath();
  }

  public void testGetUserById() throws ApiCallException {
    compareUser(defaultModel.getUsers().getUserByID(0), remoteModel.getUserByID(0));
    compareUser(defaultModel.getUsers().getUserByID(1), remoteModel.getUserByID(1));
    assertThrows(ApiCallException.class, () -> remoteModel.getUserByID(-1));
  }

  public void testAddRemoveUsers() throws ApiCallException {
    assertEquals("-1", remoteModel.addUser("foo", null, "foo", "foo")[0]);
    assertEquals("-4", remoteModel.addUser("Foo Foosen", "foo_master", "foo", "foofoo123123")[0]);
    assertEquals("-3", remoteModel.addUser("Foo Foosen", "foo_master", "foo@mail.com", "foo")[0]);
    assertEquals("-2", remoteModel.addUser("Foo Foosen", "", "foo@mail.com", "foofoo123123")[0]);
    assertEquals("-2", remoteModel.addUser("Foo Foosen", defaultModel.getUsers().getUserByID(0).getUsername(),
        "foo@mail.com", "foofoo123123")[0]);

    assertEquals("0", remoteModel.addUser("Foo Foosen", "foo_master", "foo@mail.com", "foofoo123123")[0]);
    defaultModel.getUsers().addUser("Foo Foosen", "foo_master", "foo@mail.com", "foofoo123123");
    compareUsers(defaultModel.getUsers(), remoteModel.getUsers());

    assertThrows(ApiCallException.class, () -> remoteModel.removeUserByID(-1));
    assertTrue(remoteModel.removeUserByID(2));
    defaultModel.getUsers().removeUser(2);
    compareUsers(defaultModel.getUsers(), remoteModel.getUsers());
  }

  public void testAuthenticateLogin() throws ApiCallException {
    assertTrue(remoteModel.authenticateLogin("IdaErBest", "pomeranian123"));
    assertFalse(remoteModel.authenticateLogin("IdaErBest", "pomeranien124"));
    assertFalse(remoteModel.authenticateLogin("bjarne", "pomeranian123"));
  }

  public void testModifyFields() throws ApiCallException {
    assertEquals("-1", remoteModel.changePassword(-1, "validpassword123")[0]);
    assertEquals("-1", remoteModel.changePassword(0, "foo")[0]);
    assertNull(remoteModel.changePassword(0, "validpassword123")[0]);

    assertEquals("-1", remoteModel.changeMail(-1, "valid@mail.com")[0]);
    assertEquals("-1", remoteModel.changeMail(0, "invalid")[0]);
    assertNull(remoteModel.changeMail(0, "valid@mail.com")[0]);

    assertEquals("-1", remoteModel.changeUsername(-1, "username")[0]);
    assertEquals("-1", remoteModel.changeUsername(0, "Berte92")[0]);
    assertNull(remoteModel.changeUsername(0, "username12")[0]);
  }

  public void testGetCourseByFagkode() throws ApiCallException {
    compareCourseItem(defaultModel.getCourseList().getCourseByFagkode("TMA4140"),
        remoteModel.getCourseByFagkode("TMA4140"));
    assertThrows(ApiCallException.class, () -> remoteModel.getCourseByFagkode("FOO"));
  }

  public void testGetDiscussionByFagkode() throws ApiCallException {
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        remoteModel.getDiscussion("TMA4140"));
    assertThrows(ApiCallException.class, () -> remoteModel.getDiscussion("FOO"));
  }

  public void testGetCommentById() throws ApiCallException {
    assertThrows(ApiCallException.class, () -> remoteModel.getCommentById("FOO", 0));
    assertThrows(ApiCallException.class, () -> remoteModel.getCommentById("TMA4140", -1));
    compareComment(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().getCommentByID(0),
        remoteModel.getCommentById("TMA4140", 0));
  }

  public void testAddCommentToDiscussion() throws ApiCallException {
    assertThrows(ApiCallException.class, () -> remoteModel.addCommentToDiscussion("FOO", null, null));
    assertThrows(ApiCallException.class, () -> remoteModel.addCommentToDiscussion("TMA4140", null, null));
    assertThrows(ApiCallException.class, () -> remoteModel.addCommentToDiscussion("FOO", "null", "null"));
    defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().addComment("foo", "foo");
    assertEquals(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().getPrevAssignedID(),
        remoteModel.addCommentToDiscussion("TMA4140", "foo", "foo"));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        remoteModel.getDiscussion("TMA4140"));

    defaultModel.getCourseList().getCourseByFagkode("TDT4120").getDiskusjon().addComment("foo", "foo");
    assertEquals(defaultModel.getCourseList().getCourseByFagkode("TDT4120").getDiskusjon().getPrevAssignedID(),
        remoteModel.addCommentToDiscussion("TDT4120", "foo", "foo"));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TDT4120").getDiskusjon(),
        remoteModel.getDiscussion("TDT4120"));
  }

  public void testDeleteCommentById() throws ApiCallException {
    assertThrows(ApiCallException.class, () -> remoteModel.deleteCommentByID("TMA4140", -1));
    assertThrows(ApiCallException.class, () -> remoteModel.deleteCommentByID("FOO", 0));
    defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().removeComment(3);
    assertTrue(remoteModel.deleteCommentByID("TMA4140", 3));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        remoteModel.getDiscussion("TMA4140"));
  }

  public void testUpvoteCommentById() throws ApiCallException {
    assertThrows(ApiCallException.class, () -> remoteModel.upvoteCommentByID("TMA4140", "Bobby", -1));
    assertThrows(ApiCallException.class, () -> remoteModel.upvoteCommentByID("FOO", "Bobby", 0));
    defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().upvote("Bobby", 0);
    assertTrue(remoteModel.upvoteCommentByID("TMA4140", "Bobby", 0));
    assertThrows(ApiCallException.class, () -> remoteModel.upvoteCommentByID("TMA4140", "Bobby", 0));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        remoteModel.getDiscussion("TMA4140"));
  }

  public void testDownvoteCommentById() throws ApiCallException {
    assertThrows(ApiCallException.class, () -> remoteModel.downvoteCommentByID("TMA4140", "Bobby", -1));
    assertThrows(ApiCallException.class, () -> remoteModel.downvoteCommentByID("FOO", "Bobby", 0));
    defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().downvote("Bobby", 0);
    assertTrue(remoteModel.downvoteCommentByID("TMA4140", "Bobby", 0));
    assertThrows(ApiCallException.class, () -> remoteModel.downvoteCommentByID("TMA4140", "Bobby", 0));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        remoteModel.getDiscussion("TMA4140"));
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
  public static void compareDiscussion(Discussion discussion1, Discussion discussion2) {
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
  public static void compareComment(Comment comment1, Comment comment2) {
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