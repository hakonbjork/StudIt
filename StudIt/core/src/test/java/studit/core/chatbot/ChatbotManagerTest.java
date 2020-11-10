package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.json.StuditPersistence;

public class ChatbotManagerTest {

  private ChatbotManager chatbotManager;
  private StuditModel defaultModel;

  @BeforeEach
  public void init() {
    try (Reader reader = new FileReader("../res/db/studitModel.json", StandardCharsets.UTF_8)) {
      defaultModel = new StuditPersistence().readStuditModel(reader);
    } catch (IOException e) {
      System.out.println("Couldn't read default studitModel");
    }

    chatbotManager = new ChatbotManager(defaultModel.getCourseList().getCourseNameList());

  }

  @Test
  public void testManageInput() {
    assertEquals( "Hei! ", chatbotManager.manageInput("Hei!").getResponse());
    
    Response noMatchResponse = chatbotManager.manageInput("Dette er bare vissvass");
    assertEquals(defaultResponse, noMatchResponse.getResponse());
    assertFalse(noMatchResponse.funcCall());

    Response anbefaltResponse = chatbotManager.manageInput("Hva er anbefalt lesestoff i Statistikk?");
    assertEquals("", anbefaltResponse.getResponse());
    assertTrue(anbefaltResponse.funcCall());
    assertEquals("anbefalt", anbefaltResponse.getFuncKey());
  }

  /**
   *  ---------------------------------------- Responses --------------------------------------------
   */

  private String defaultResponse = "Jeg beklager, men det forstod jeg ikke helt. Prøv å formulere setningen på en annen måte. Skriv 'jeg trenger hjelp' hvis du står fast";
}
