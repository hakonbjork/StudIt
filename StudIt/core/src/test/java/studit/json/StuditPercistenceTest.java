package studit.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.core.mainpage.Comment;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.User;
import studit.core.users.Users;

public class StuditPercistenceTest {

  private static final String TEST_PATH = "src/main/resources/studit/db/studitModelTest.json";
  private StuditPersistence studitPersistence = new StuditPersistence();

  @Test
  public void testSerializersDeserializers() {

    StuditModel testStuditModel = genTestStuditModel();

    File file = new File(TEST_PATH);
    Writer writer;

    try {
      writer = new PrintWriter(file);
      studitPersistence.writeStuditModel(testStuditModel, writer);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    StuditModel loadedModel = loadTestModel();

    CourseList testCourseList = testStuditModel.getCourseList();
    CourseList loadedCourseList = loadedModel.getCourseList();

    // We've added two objects to our CourseList, so we iterate two times
    for (int i = 0; i < 2; i++) {
      assertEquals(testCourseList.iterator().hasNext(), loadedCourseList.iterator().hasNext());
      CourseItem nextTestCourseItem = testCourseList.iterator().next();
      CourseItem nextLoadedCourseItem = loadedCourseList.iterator().next();

      assertEquals(nextTestCourseItem.getFagkode(), nextLoadedCourseItem.getFagkode());
      assertEquals(nextTestCourseItem.getInformasjon(), nextLoadedCourseItem.getInformasjon());
      assertEquals(nextTestCourseItem.getPensumlitteratur(), nextLoadedCourseItem.getPensumlitteratur());
      assertEquals(nextTestCourseItem.getAnbefaltLitteratur(), nextLoadedCourseItem.getAnbefaltLitteratur());
      assertEquals(nextTestCourseItem.getTips(), nextLoadedCourseItem.getTips());
      assertEquals(nextTestCourseItem.getEksamensdato(), nextLoadedCourseItem.getEksamensdato());
      assertEquals(nextTestCourseItem.getVurderingsform(), nextLoadedCourseItem.getVurderingsform());
      assertEquals(nextTestCourseItem.getHjelpemidler(), nextLoadedCourseItem.getHjelpemidler());
      assertEquals(nextTestCourseItem.getAverageVurdering(), nextLoadedCourseItem.getAverageVurdering());
      assertEquals(nextTestCourseItem.getVurderinger(), nextLoadedCourseItem.getVurderinger());

      Discussion testDiscussion = nextTestCourseItem.getDiskusjon();
      Discussion loadedDiscussion = nextLoadedCourseItem.getDiskusjon();
      Map<Integer, Comment> loadedComments = loadedDiscussion.getComments();

      assertEquals(testDiscussion.getPrevAssignedID(), loadedDiscussion.getPrevAssignedID());

      for (Entry<Integer, Comment> testEntry : testDiscussion.getComments().entrySet()) {
        Comment testComment = testEntry.getValue();
        Comment loadedComment = loadedComments.get(testEntry.getKey());
        assertNotNull(loadedComment, "ID mismatch between comments hashmaps");

        assertEquals(testComment.getBrukernavn(), loadedComment.getBrukernavn());
        assertEquals(testComment.getKommentar(), loadedComment.getKommentar());
        assertEquals(testComment.getDato(), loadedComment.getDato());
        assertEquals(testComment.getUpvotes(), loadedComment.getUpvotes());
        assertEquals(testComment.getDownvotes(), loadedComment.getDownvotes());
        assertEquals(testComment.getUniqueID(), loadedComment.getUniqueID());
        assertTrue(testComment.getUpvoters().equals(testComment.getUpvoters()));
        assertTrue(testComment.getDownvoters().equals(testComment.getDownvoters()));

      }
    }

    Users testUsers = testStuditModel.getUsers();
    Users loadedUsers = loadedModel.getUsers();

    assertNotNull(loadedUsers);
    assertEquals(testUsers.getPrevAssignedID(), loadedUsers.getPrevAssignedID());

    Map<Integer, User> testUsersMap = testUsers.getUsers();
    Map<Integer, User> loadedUsersMap = loadedUsers.getUsers();

    // Iterate two times, as we added 2 test users
    for (int i = 0; i < 2; i++) {
      User testUser = testUsersMap.get(i);
      User loadedUser = loadedUsersMap.get(i);

      assertEquals(testUser.getName(), loadedUser.getName());
      assertEquals(testUser.getUsername(), loadedUser.getUsername());
      assertEquals(testUser.getMail(), loadedUser.getMail());
      assertEquals(testUser.getPassword(), loadedUser.getPassword());
      assertEquals(testUser.getUniqueID(), loadedUser.getUniqueID());
    }

  }

  private StuditModel loadTestModel() {
    try (FileReader reader = new FileReader(TEST_PATH, StandardCharsets.UTF_8)) {

      StuditModel model = studitPersistence.readStuditModel(reader);
      return model;

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private StuditModel genTestStuditModel() {

    CourseItem testItem = new CourseItem();

    testItem.setFagkode("TMA4140");
    testItem.setFagnavn("Diskret Matematikk 1");
    testItem.setInformasjon("Info");
    testItem.setPensumlitteratur("Mathematics for dummies and retards");
    testItem.setAnbefaltLitteratur("Kompendium Diskmat");
    testItem.setTips("Ikke kok");
    testItem.setEksamensdato("03/12/2020");
    testItem.setVurderingsform("Hjemmeksamen");
    testItem.setHjelpemidler("alle");
    testItem.setVurderinger(List.of(3, 5, 9, 8));

    Discussion discussion = new Discussion();
    int id1 = discussion.addComment("BobbyBigBoi", "Bra saker!");
    discussion.addComment("BobbyBigBoi", "Ok saker :/");
    int id3 = discussion.addComment("BjarteBrorMor", "Bobby er en idiot!");

    discussion.upvote("BobbyBigBoi", id1);
    discussion.upvote("BobbyBigBoi", id1);
    discussion.downvote("BjarteBrorMor", id1);
    discussion.upvote("BjarteBrorMor", id3);
    discussion.upvote("BobbyBigBoi", id3);

    testItem.setDiskusjon(discussion);

    CourseItem testItem2 = new CourseItem();
    testItem2.setFagkode("TDT4120");
    testItem2.setFagnavn("Informasjonsteknologi - Avansert kurs");
    testItem2.setInformasjon("Masse informasjon her");
    testItem2.setPensumlitteratur("Informatics for dummies and retards");
    testItem2.setAnbefaltLitteratur("Kompendium IT");
    testItem2.setTips("Ikke kok. Spis grønnsaker");
    testItem2.setEksamensdato("05/12/2020");
    testItem2.setVurderingsform("Muntlig eksamen");
    testItem2.setHjelpemidler("Mamma på telefon");
    testItem2.setVurderinger(List.of(3, 5, 9, 8));

    Discussion discussion2 = new Discussion();
    discussion.addComment("WhamMaster28", "Dinglepop!");
    testItem2.setDiskusjon(discussion2);

    CourseList courseList = new CourseList();
    courseList.addCourseItem(testItem);
    courseList.addCourseItem(testItem2);

    StuditModel model = new StuditModel();
    model.setCourseList(courseList);

    Users users = new Users();
    users.addUser("Berte bjernsen", "Berte92", "berte@bertebok.com", "kusma1992");
    users.addUser("Ida Idasen", "IdaErBest", "IdaElskerHunder@flyskflysk.com", "pomeranian123");

    // The passwords will be hashed and uhashed, check that this logic works.
    assertTrue(users.authenticateLogin("Berte92", "kusma1992"));
    assertTrue(users.authenticateLogin("IdaErBest", "pomeranian123"));
    model.setUsers(users);

    return model;
  }

}