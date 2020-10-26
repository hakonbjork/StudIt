package studit.restserver;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.json.StuditPersistence;

public class StuditConfigTest {

  private static final String DBPATH = "src/test/resources/studit/restserver/testdb.json";
  // This is a simple test class checking that model initalization is handled
  // correctly.
  // Extensive testing of functionallity is checked under restapi

  /**
   * Check that the config correctly loads and reads studitmodel.
   */
  @Test
  public void testStuditConfig() {
    StuditConfig studitConfig = new StuditConfig(DBPATH);
    assertNotNull(studitConfig.getStuditModel());
    StuditModel model = studitConfig.getStuditModel();

    StuditModel loadedModel = StuditConfig.loadModel(DBPATH);
    assertNotNull(loadedModel);
    assertEquals(model.getUsers().getPrevAssignedID(), loadedModel.getUsers().getPrevAssignedID());

  }

  /**
   * Check that default StuditModel is not null.
   */
  @Test
  public void testCreateDefaultStuditModel() {
    StuditPersistence studitPersistence = new StuditPersistence();
    StuditModel model = StuditConfig.createDefaultStuditModel(studitPersistence, DBPATH);
    assertNotNull(model);
  }

}