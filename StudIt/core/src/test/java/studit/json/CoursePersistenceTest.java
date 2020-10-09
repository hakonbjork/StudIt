package studit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.core.mainpage.CourseModel;

public class CoursePersistenceTest {

  private CoursePersistence coursePersistence = new CoursePersistence();

  @BeforeAll
  public static void setUp() {


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

    

  }
}