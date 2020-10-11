package studit.json;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
  private Collection<CourseItem> loadData() {

    try (FileReader fr = new FileReader("src/main/resources/studit/db/maindbtest.json", StandardCharsets.UTF_8)) {
      
      CourseList li = coursePersistence.readCourseList(fr);

      System.out.println(li.getCourseItems().size());

      Collection<CourseItem> items = li.getCourseItems();

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
    item1.setKommentar("Schpa fag");
    item1.setScore("10");
    list.addCourseItem(item1);

    CourseItem item2 = new CourseItem();
    item2.setFagkode("TMA4125");
    item2.setFagnavn("Statistikk");
    item2.setKommentar("Kult fag");
    item2.setScore("8");
    list.addCourseItem(item2);

    CourseList courseList = new CourseList();
    courseList.addCourseItem(item1);
    courseList.addCourseItem(item2);

    Collection<CourseItem> items2 = loadData();

    Collection<CourseItem> items1 = courseList.getCourseItems();

    assertEquals(items1.iterator().hasNext(), items2.iterator().hasNext());
    assertEquals(items1.iterator().next(), items2.iterator().next());

    assertEquals(items1.iterator().hasNext(), items2.iterator().hasNext());
    assertEquals(items1.iterator().next(), items2.iterator().next());

    assertFalse(items1.iterator().hasNext());
    assertFalse(items2.iterator().hasNext());

    
    

    

  }
}