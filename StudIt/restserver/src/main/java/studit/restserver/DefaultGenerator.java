package studit.restserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import studit.core.StuditModel;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.core.users.Users;
import studit.json.StuditPersistence;

public class DefaultGenerator {

  private static final String DEFAULT_PATH = "res/db/default/defaultModel.json";

  public static StuditModel writeDefaultDataToDb(String path) {
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

    CourseList courseList = new CourseList();
    courseList.addCourseItem(testItem);
    courseList.addCourseItem(testItem2);

    StuditModel model = new StuditModel();
    model.setCourseList(courseList);

    Users users = new Users();
    users.addUser("Berte bjernsen", "Berte92", "berte@bertebok.com", "kusma1992");
    users.addUser("Ida Idasen", "IdaErBest", "IdaElskerHunder@flyskflysk.com", "pomeranian123");
    model.setUsers(users);

    StuditPersistence studitPersistence = new StuditPersistence();
    try {
      Writer writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
      studitPersistence.writeStuditModel(model, writer);

    } catch (IOException e) {
      return new StuditModel();
    }

    return model;
  }

  public static StuditModel writeDefaultDataToDb() {
    return writeDefaultDataToDb(DEFAULT_PATH);
  }
}