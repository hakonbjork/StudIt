package studit.ui.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareCourseLists;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareUser;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareUsers;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareCourseItem;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareDiscussion;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareComment;
import static studit.ui.remote.RemoteStuditModelAccesTest.compareStuditModels;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import studit.core.StuditModel;
import studit.json.StuditPersistence;

public class DirectStuditModelAccessTest {

  DirectStuditModelAccess directModel = new DirectStuditModelAccess();
  StuditModel defaultModel;

  @BeforeEach
  public void init() {
    try (Reader reader = new FileReader("src/test/resources/studit/fxui/defaultdb.json", StandardCharsets.UTF_8)) {
      defaultModel = new StuditPersistence().readStuditModel(reader);
    } catch (IOException e) {
      System.out.println("Couldn't read default studitModel");
    }
  }

  @Test
  public void testGetStuditModel() throws ApiCallException, IOException {
    assertNotNull(defaultModel);
    compareStuditModels(defaultModel, directModel.getStuditModel());
    assertTrue(directModel.ping());
  }

  @Test
  public void testGetCourseList() throws ApiCallException {
    compareCourseLists(defaultModel.getCourseList(), directModel.getCourseList());
  }

  @Test
  public void testGetUsers() throws ApiCallException {
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
  }

  @Test
  public void testGetUserByID() throws ApiCallException {
    compareUser(defaultModel.getUsers().getUserByID(0), directModel.getUserByID(0));
  }

  @Test
  public void testRemoveUserByID() throws ApiCallException {
    assertEquals(defaultModel.getUsers().removeUser(0), directModel.removeUserByID(0));
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
  }

  @Test
  public void testAddUser() throws ApiCallException {
    String[] result = defaultModel.getUsers().addUser("foo", "foo_master", "foo@mail.com", "foo");
    String[] response = directModel.addUser("foo", "foo_master", "foo@mail.com", "foo");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertEquals(result[2], response[0]);
    assertEquals(result[1], response[1]);
  }

  @Test
  public void testAuthenticateLogin() throws ApiCallException {
    assertEquals(defaultModel.getUsers().authenticateLogin("IdaErBest", "pomeranian123"),
        directModel.authenticateLogin("IdaErBest", "pomeranian123"));
    assertEquals(defaultModel.getUsers().authenticateLogin("foo", "foo"), directModel.authenticateLogin("foo", "foo"));
  }

  @Test
  public void testChangePassword() throws ApiCallException {
    defaultModel.getUsers().changePassword(0, "validpassword123");
    String[] response = directModel.changePassword(0, "validpassword123");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertNull(response[0]);
    assertEquals("Success", response[1]);

    defaultModel.getUsers().changePassword(0, "foo");
    String[] response2 = directModel.changePassword(0, "foo");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertNotNull(response2[0]);
    assertNotNull(response2[1]);
  }

  @Test
  public void testChangeUsername() throws ApiCallException {
    defaultModel.getUsers().changeUsername(0, "valid_username");
    String[] response = directModel.changeUsername(0, "valid_username");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertNull(response[0]);
    assertEquals("Success", response[1]);

    defaultModel.getUsers().changeUsername(0, "Berte92");
    String[] response2 = directModel.changeUsername(0, "Berte92");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertNotNull(response2[0]);
    assertNotNull(response2[1]);
  }

  @Test
  public void testChangeMail() throws ApiCallException {
    defaultModel.getUsers().changeMail(0, "foo@mail.com");
    String[] response = directModel.changeMail(0, "foo@mail.com");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertNull(response[0]);
    assertEquals("Success", response[1]);

    defaultModel.getUsers().changeMail(0, "foo");
    String[] response2 = directModel.changeMail(0, "foo");
    compareUsers(defaultModel.getUsers(), directModel.getUsers());
    assertNotNull(response2[0]);
    assertNotNull(response2[1]);
  }

  @Test
  public void testGetCourseByFagkode() throws ApiCallException {
    compareCourseItem(defaultModel.getCourseList().getCourseByFagkode("TMA4140"),
        directModel.getCourseByFagkode("TMA4140"));
  }

  @Test
  public void testGetDiscussion() throws ApiCallException {
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        directModel.getDiscussion("TMA4140"));
    assertThrows(ApiCallException.class, () -> directModel.getDiscussion("FOO"));
  }

  @Test
  public void testAddcommentToDiscussion() throws ApiCallException {
    assertEquals(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().addComment("foo", "foo"),
        directModel.addCommentToDiscussion("TMA4140", "foo", "foo"));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        directModel.getDiscussion("TMA4140"));
    assertThrows(ApiCallException.class, () -> directModel.addCommentToDiscussion("FOO", "foo", "foo"));

  }

  @Test
  public void testGetCommentByID() throws ApiCallException {
    compareComment(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().getCommentByID(0),
        directModel.getCommentById("TMA4140", 0));

    assertThrows(ApiCallException.class, () -> directModel.getCommentById("FOO", 0));
  }

  @Test
  public void testDeleteCommentById() throws ApiCallException {
    assertEquals(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().removeComment(0),
        directModel.deleteCommentByID("TMA4140", 0));
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        directModel.getDiscussion("TMA4140"));
    assertThrows(ApiCallException.class, () -> directModel.deleteCommentByID("FOO", 0));
  }

  @Test
  public void testUpvoteComment() throws ApiCallException {
    assertTrue(directModel.upvoteCommentByID("TMA4140", "foo", 0));
    assertFalse(directModel.upvoteCommentByID("TMA4140", "foo", 0));

    defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().upvote("foo", 0);
    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        directModel.getDiscussion("TMA4140"));

    assertThrows(ApiCallException.class, () -> directModel.upvoteCommentByID("FOO", "foo", 0));
  }

  @Test
  public void testDownvoteComment() throws ApiCallException {
    assertTrue(directModel.downvoteCommentByID("TMA4140", "foo", 0));
    assertFalse(directModel.downvoteCommentByID("TMA4140", "foo", 0));

    defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon().downvote("foo", 0);

    compareDiscussion(defaultModel.getCourseList().getCourseByFagkode("TMA4140").getDiskusjon(),
        directModel.getDiscussion("TMA4140"));

    assertThrows(ApiCallException.class, () -> directModel.downvoteCommentByID("FOO", "foo", 0));
  }

}