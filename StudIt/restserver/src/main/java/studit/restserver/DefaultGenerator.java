package studit.restserver;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import studit.core.StuditModel;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.Discussion;
import studit.json.StuditPersistence;

public class DefaultGenerator {

  private static final String DEFAULT_PATH = "res/db/default/defaultModel.json";

  public static void writeDefaultDataToDb() {
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

    discussion.upvote(id1);
    discussion.downvote(id1);
    discussion.upvote(id3);
    discussion.upvote(id3);

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

    StuditPersistence studitPersistence = new StuditPersistence();
    try {
      Writer writer = new OutputStreamWriter(new FileOutputStream(DEFAULT_PATH), StandardCharsets.UTF_8);
      studitPersistence.writeStuditModel(model, writer);

    } catch (IOException e) {
      e.printStackTrace();
    }

    Reader reader = null;
    try {
      reader = new FileReader(DEFAULT_PATH, StandardCharsets.UTF_8);
      StuditModel loadedModel = studitPersistence.readStuditModel(reader);
      System.out.println(loadedModel.getCourseList().getCourseItems().get(0).getDiskusjon());
      //System.out.println(loadedModel.getCourseList().getCourseItems().get(0).getDiskusjon().getComments().get(0));

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }
}