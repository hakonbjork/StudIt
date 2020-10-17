package studit.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;

public class CoursePersistenceTest {

  private CoursePersistence coursePersistence = new CoursePersistence();

  @BeforeAll
  public static void setUp() {

  }

  /**
   * This function should fetch data from a database
   */
  private List<CourseItem> loadData() {

    try (FileReader fr = new FileReader("src/main/resources/studit/db/maindbtest.json", StandardCharsets.UTF_8)) {

      CourseList li = coursePersistence.readCourseList(fr);

      System.out.println(li.getCourseItems().size());

      List<CourseItem> items = li.getCourseItems();

      return items;

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;

  }

  @Test
  public void testSerializersDeserializers() {

    CourseList list = new CourseList();

    CourseItem item1 = new CourseItem();
    item1.setFagkode("TDT4109");
    item1.setFagnavn("ITGK for grovinger");
    item1.setInformasjon("Schpa fag");
    list.addCourseItem(item1);

    CourseItem item2 = new CourseItem();
    item2.setFagkode("TMA4125");
    item2.setFagnavn("Statistikk");
    item2.setInformasjon("Kult fag");
    list.addCourseItem(item2);

    CourseList courseList = new CourseList();
    courseList.addCourseItem(item1);
    courseList.addCourseItem(item2);

    File file = new File("src/main/resources/studit/db/maindbtest.json");
    Writer writer;
    try {
      writer = new PrintWriter(file);
      coursePersistence.writeCourseList(courseList, writer);

    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    List<CourseItem> items2 = loadData();
    List<CourseItem> items1 = courseList.getCourseItems();
    System.out.println("Hello: " + items2.get(0).getFagnavn());

    assertEquals(items1.iterator().hasNext(), items2.iterator().hasNext());
    assertEquals(items1.iterator().next().getFagnavn(), items2.iterator().next().getFagnavn());

    assertEquals(items1.iterator().hasNext(), items2.iterator().hasNext());
    assertEquals(items1.iterator().next().getFagnavn(), items2.iterator().next().getFagnavn());

  }
}