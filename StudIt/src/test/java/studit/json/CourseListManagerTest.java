package studit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CourseListManagerTest{


    private CourseListManager courseListManager;

    @BeforeAll
    public static void setUp() {

    }


    @Test
    public void testWriteToDb(){

        courseListManager = new CourseListManager();

        Map<String, String[]> load = courseListManager.loadJson("db.json");

        assertFalse(load.isEmpty());

        load.size();

    }



}